package com.yuk.kinesisgui

import com.amazonaws.services.kinesis.clientlibrary.types.UserRecord
import com.yuk.kinesisgui.processor.RecordProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.time.LocalDateTime

class StreamTracker(
    private val kinesisService: KinesisService
) {
    private lateinit var streamName: String
    private val shardTrackerMap = mutableMapOf<String, ShardTracker>()
    private val recordProcessors = mutableListOf<RecordProcessor>()

    fun start(
        streamName: String,
        trimHorizon: Boolean,
        afterTime: LocalDateTime?
    ) {
        this.streamName = streamName
        setShardTracker(trimHorizon, afterTime)
    }

    fun stop() {
        shardTrackerMap.forEach { (_, shardTracker) ->
            shardTracker.stop()
        }
    }

    private fun setShardTracker(trimHorizon: Boolean, afterTime: LocalDateTime?) {
        val ids = kinesisService.getShardIds(streamName)

        val shardIteratorType = when {
            trimHorizon -> "TRIM_HORIZON"
            afterTime != null -> "AT_TIMESTAMP"
            else -> "LATEST"
        }

        val searchDate = Timestamp.valueOf(afterTime ?: LocalDateTime.now())

        ids.map { id ->
            val iter = kinesisService.getShardIterator(streamName, id, shardIteratorType, searchDate)
            val tracker = ShardTracker(kinesisService, iter, id)

            shardTrackerMap[id] = tracker
            tracker.start()
        }
    }

    fun addRecordProcessor(recordProcessor: RecordProcessor) {
        recordProcessors.add(recordProcessor)
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

    class ShardTracker(
        private val kinesisService: KinesisService,
        private var iterator: String,
        private var shardId: String
    ) {
        private var isRunning = false
        private var recordProcessors = mutableListOf<RecordProcessor>()

        fun setRecordProcessors(recordProcessors: MutableList<RecordProcessor>) {
            this.recordProcessors = recordProcessors
        }

        fun stop() {
            isRunning = false
        }

        fun start() {
            if (isRunning.not()) {
                isRunning = true
                getRecords()
            }
        }

        private fun getRecords() {
            CoroutineScope(Dispatchers.IO).launch {
                while (isRunning) {
                    val result = kinesisService.getRecords(iterator, 1000)
                    val deaggregationRecords = UserRecord.deaggregate(result.records)

                    val records: Set<RecordData> = deaggregationRecords.flatMapTo(mutableSetOf()) { record ->
                        try {
                            Config.objectMapper.readerFor(RecordData::class.java).readValues<RecordData>(record.data.array())
                                .asSequence()
                                .map {
                                    it.seq = "${record.sequenceNumber}:${record.subSequenceNumber}"
                                    it.recordTime = record.approximateArrivalTimestamp.toString()
                                    it.shardId = shardId
                                    it.partitionKey = record.partitionKey
                                    it
                                }
                                .toSet()
                        } catch (e: Exception) {
                            println("seq: ${record.sequenceNumber} message: ${e.message}")
                            return@flatMapTo emptySet()
                        }
                    }

                    recordProcessors.forEach {
                        it.process(records)
                    }

                    if (result.nextShardIterator == null) {
                        isRunning = false
                    } else {
                        iterator = result.nextShardIterator
                    }

                    delay(1000)
                }
            }
        }
    }
}
