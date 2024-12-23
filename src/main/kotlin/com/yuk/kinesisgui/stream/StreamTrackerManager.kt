package com.yuk.kinesisgui.stream

import java.time.LocalDateTime

@Deprecated("", level = DeprecationLevel.HIDDEN)
class StreamTrackerManager(
    private val kinesisService: KinesisService,
) {
    private val streamTrackerMap = mutableMapOf<String, StreamTracker>()

    fun startTracking(
        streamName: String,
        trimHorizon: Boolean = false,
        afterTime: LocalDateTime? = null,
    ) {
        if (streamTrackerMap.containsKey(streamName)) {
            throw IllegalStateException("Stream $streamName is already tracked")
        }

        val streamTracker = StreamTracker(kinesisService)

        streamTracker.start(streamName, trimHorizon, afterTime)
        streamTrackerMap[streamName] = streamTracker
    }

    fun stopTracking(streamName: String) {
        val streamTracker =
            streamTrackerMap.remove(streamName)
                ?: throw IllegalStateException("Stream $streamName is not tracked")

        streamTracker.stop()
    }

    fun isTracked(streamName: String): Boolean {
        return streamTrackerMap.containsKey(streamName)
    }

    fun addRecordProcessor(
        streamName: String,
        recordProcessor: RecordProcessor,
    ) {
        if (isTracked(streamName).not()) {
            throw IllegalStateException("Stream $streamName is not tracked")
        }

        streamTrackerMap[streamName]?.addRecordProcessor(recordProcessor)
    }

    fun removeRecordProcessor(
        streamName: String,
        recordProcessor: RecordProcessor,
    ) {
        val stopped = streamTrackerMap[streamName]?.removeRecordProcessor(recordProcessor)

        if (stopped == true) {
            streamTrackerMap.remove(streamName)
        }
    }
}
