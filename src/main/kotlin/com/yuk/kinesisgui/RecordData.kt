package com.yuk.kinesisgui

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

class RecordData(
    val eventTime: String,
    val eventType: String,
    val source: String,
    @JsonProperty("data")
    data: Map<String, Any?>
) : Comparable<RecordData> {
    @JsonIgnore
    val data = data.toString()
    @JsonIgnore
    lateinit var recordTime: String
    @JsonIgnore
    lateinit var seq: String
    @JsonIgnore
    lateinit var shardId: String
    @JsonIgnore
    lateinit var partitionKey: String

    override fun compareTo(other: RecordData): Int {
        return eventTime.compareTo(other.eventTime)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecordData

        if (eventType != other.eventType) return false
        if (data != other.data) return false
        if (seq != other.seq) return false
        if (shardId != other.shardId) return false
        if (partitionKey != other.partitionKey) return false

        return true
    }

    override fun hashCode(): Int {
        var result = eventType.hashCode()
        result = 31 * result + data.hashCode()
        result = 31 * result + seq.hashCode()
        result = 31 * result + shardId.hashCode()
        result = 31 * result + partitionKey.hashCode()
        return result
    }
}
