package com.yuk.kinesisgui

import com.yuk.kinesisgui.processor.RecordProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StreamTracker(
    private val kinesisService: KinesisService
) {
    private lateinit var streamName: String
    private val shardTrackerMap = mutableMapOf<String, ShardTracker>()
    private val recordProcessors = mutableListOf<RecordProcessor>()

    fun start(streamName: String) {
        this.streamName = streamName
        setShardTracker()
    }

    fun stop() {
        shardTrackerMap.forEach { (_, shardTracker) ->
            shardTracker.stop()
        }
    }

    private fun setShardTracker() {
        val ids = kinesisService.getShardIds(streamName)
        ids.map { id ->
            val iter = kinesisService.getShardIterator(streamName, id)
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
                    val result = kinesisService.getRecords(iterator, 100)

                    result.records.map { String(it.data.array()) }.forEach { record ->
                        recordProcessors.forEach { it.process(record) }
                    }

                    iterator = result.nextShardIterator

                    delay(1000)
                }
            }
        }
    }
}
