package com.yuk.kinesisgui

import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Service

@Service
@Scope("prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
class StreamTracker(
    private val kinesisService: KinesisService
) {
    private lateinit var streamName: String

    fun start(streamName: String) {
        this.streamName = streamName

    }

    fun stop() {
    }
}