package com.yuk.kinesisgui

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

class RecordData(
    val eventTime: String,
    val eventType: String,
    val source: String,
    @JsonProperty("data")
    data: Map<String, Any?>
) {
    @JsonIgnore
    val data = data.toString()
    @JsonIgnore
    lateinit var recordTime: String
    @JsonIgnore
    lateinit var seq: String
}
