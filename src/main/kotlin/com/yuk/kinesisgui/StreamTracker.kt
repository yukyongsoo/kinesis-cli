package com.yuk.kinesisgui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Service

@Service
class StreamTracker(
    private val kinesisService: KinesisService,
    private val recordProcessor: RecordProcessor
) {
    private lateinit var streamName: String
    private val shardTrackerMap = mutableMapOf<String, ShardTracker>()

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
            val tracker = ShardTracker(kinesisService, recordProcessor, iter)

            shardTrackerMap[id] = tracker
            tracker.start()
        }
    }

    class ShardTracker(
        private val kinesisService: KinesisService,
        private val recordProcessor: RecordProcessor,
        private var iterator: String
    ) {
        private var isRunning = false

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

                    result.records.map { String(it.data.array()) }.forEach {
                        recordProcessor.processRecord(it)
                    }

                    iterator = result.nextShardIterator

                    delay(1000)
                }
            }
        }
    }
}
