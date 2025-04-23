package com.yuk.kinesisgui.stream

import com.fasterxml.jackson.module.kotlin.readValue
import com.yuk.kinesisgui.Config
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import software.amazon.awssdk.services.kinesis.model.GetRecordsResponse
import software.amazon.kinesis.retrieval.AggregatorUtil
import software.amazon.kinesis.retrieval.KinesisClientRecord
import java.nio.charset.StandardCharsets
import java.sql.Timestamp
import java.time.LocalDateTime

class StreamTracker(
    private val kinesisService: KinesisService,
) {
    private lateinit var streamName: String
    private val shardTrackerMap = mutableMapOf<String, ShardTracker>()
    private val recordProcessors = mutableListOf<RecordProcessor>()

    fun start(
        streamName: String,
        trimHorizon: Boolean,
        afterTime: LocalDateTime?,
    ) {
        this.streamName = streamName
        setShardTracker(streamName, trimHorizon, afterTime)
        registerRecordProcessor()
    }

    fun stop() {
        shardTrackerMap.forEach { (_, shardTracker) ->
            shardTracker.stop()
        }

        shardTrackerMap.clear()
    }

    private fun setShardTracker(
        streamName: String,
        trimHorizon: Boolean,
        afterTime: LocalDateTime?,
    ) {
        val ids = kinesisService.getShardIds(this.streamName)

        val shardIteratorType =
            when {
                trimHorizon -> "TRIM_HORIZON"
                afterTime != null -> "AT_TIMESTAMP"
                else -> "LATEST"
            }

        val searchDate = Timestamp.valueOf(afterTime ?: LocalDateTime.now())

        ids.map { id ->
            val iter = kinesisService.getShardIterator(this.streamName, id, shardIteratorType, searchDate)
            val tracker = ShardTracker(kinesisService, streamName, iter, id)

            shardTrackerMap[id] = tracker
            tracker.start()
        }
    }

    private fun registerRecordProcessor() {
        shardTrackerMap.forEach {
                (_, shardTracker) ->
            shardTracker.setRecordProcessors(recordProcessors)
        }
    }

    fun addRecordProcessor(recordProcessor: RecordProcessor) {
        if (shardTrackerMap.isEmpty()) {
            throw IllegalStateException("tracker is not started")
        }

        if (recordProcessors.contains(recordProcessor).not()) {
            recordProcessors.add(recordProcessor)
        }

        shardTrackerMap.forEach {
                (_, shardTracker) ->
            shardTracker.setRecordProcessors(recordProcessors)
        }
    }

    fun removeRecordProcessor(recordProcessor: RecordProcessor): Boolean {
        recordProcessors.remove(recordProcessor)

        return if (recordProcessors.isEmpty()) {
            shardTrackerMap.forEach { (_, shardTracker) ->
                shardTracker.stop()
            }
            true
        } else {
            shardTrackerMap.forEach {
                    (_, shardTracker) ->
                shardTracker.setRecordProcessors(recordProcessors)
            }
            false
        }
    }

    inner class ShardTracker(
        private val kinesisService: KinesisService,
        private val stream: String,
        private var iterator: String,
        private var shardId: String,
    ) {
        private var isRunning = false
        private var recordProcessors = mutableListOf<RecordProcessor>()
        private lateinit var currentJob: Job

        fun setRecordProcessors(recordProcessors: MutableList<RecordProcessor>) {
            this.recordProcessors = recordProcessors
        }

        fun stop() {
            runBlocking {
                isRunning = false
                if (this@ShardTracker::currentJob.isInitialized) {
                    currentJob.cancelAndJoin()
                }
            }
        }

        fun start() {
            if (isRunning.not()) {
                isRunning = true
                getRecords()
            }
        }

        private fun getRecords() {
            currentJob =
                CoroutineScope(Dispatchers.IO).launch {
                    delay(5000)

                    while (isRunning) {
                        delay(1000)

                        val result = kinesisService.getRecords(iterator, 1000)
                        val deaggregationRecords = deaggregateRecord(result)

                        val records =
                            deaggregationRecords.mapNotNullTo(mutableSetOf()) { record ->
                                try {
                                    val raw = StandardCharsets.UTF_8.decode(record.data()).toString()
                                    val values = Config.objectMapper.readValue<MutableMap<String, Any?>>(raw)

                                    RecordData(values).apply {
                                        seq = "${record.sequenceNumber()}:${record.subSequenceNumber()}"
                                        recordTime = record.approximateArrivalTimestamp().toString()
                                        this.shardId = this@ShardTracker.shardId
                                        partitionKey = record.partitionKey()
                                        this.raw = raw
                                    }
                                } catch (e: Exception) {
                                    println("seq: ${record.sequenceNumber()} message: ${e.message}")
                                    return@mapNotNullTo null
                                }
                            }

                        if (recordProcessors.isEmpty()) {
                            println("recordProcessors is empty")
                        }

                        recordProcessors.forEach {
                            it.process(records, iterator)
                        }

                        if (result.nextShardIterator() == null) {
                            isRunning = false
                        } else {
                            iterator = result.nextShardIterator()
                        }
                    }
                }
        }
    }

    private val aggregatorUtil = AggregatorUtil()

    private fun deaggregateRecord(result: GetRecordsResponse): List<KinesisClientRecord> {
        val records =
            result.records().map {
                KinesisClientRecord.fromRecord(it)
            }

        return aggregatorUtil.deaggregate(records)
    }
}
