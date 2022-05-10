package com.yuk.kinesisgui.gui

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

class EventData(
    val eventTime: String,
    val eventType: String,
    val source: String,
    @JsonProperty("data")
    data: Map<String, Any?>
) {
    @JsonIgnore
    val data = data.toString()
}
