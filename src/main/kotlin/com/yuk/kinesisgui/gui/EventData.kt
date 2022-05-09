package com.yuk.kinesisgui.gui

data class EventData(
    val eventTime: String,
    val eventType: String,
    val source: String,
    val data: Map<String, Any?>
)
