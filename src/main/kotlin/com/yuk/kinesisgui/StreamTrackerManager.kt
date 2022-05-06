package com.yuk.kinesisgui

import org.springframework.stereotype.Service

@Service
class StreamTrackerManager(
    private val streamTracker: StreamTracker
) {
    private val streamTrackerMap = mutableMapOf<String, StreamTracker>()

    fun startTracking(streamName: String) {
        if (streamTrackerMap.containsKey(streamName)) {
            throw IllegalStateException("Stream $streamName is already tracked")
        }

        streamTracker.start(streamName)
        streamTrackerMap[streamName] = streamTracker
    }

    fun stopTracking(streamName: String) {
        streamTrackerMap.remove(streamName)?.stop()
    }
}
