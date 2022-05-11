package com.yuk.kinesisgui

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
            val tracker = ShardTracker(kinesisService, iter)

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
        private var iterator: String
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

                    val records = result.records.flatMap { record ->
                        try {
                            Config.objectMapper.readerFor(RecordData::class.java).readValues<RecordData>(record.data.array())
                                .asSequence()
                                .map {
                                    it.seq = record.sequenceNumber
                                    it.recordTime = record.approximateArrivalTimestamp.toString()
                                    it
                                }
                                .toList()
                        }
                        catch (e: Exception) {
                            println("seq: ${record.sequenceNumber} message: ${e.message}")
                            return@flatMap emptyList()
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
