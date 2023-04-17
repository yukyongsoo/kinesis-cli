package com.yuk.kinesisgui.stream

import com.fasterxml.jackson.annotation.JsonIgnore

class RecordData(
    val values: MutableMap<String, Any?>
) : Comparable<RecordData> {
    @JsonIgnore
    lateinit var recordTime: String
    @JsonIgnore
    lateinit var seq: String
    @JsonIgnore
    lateinit var shardId: String
    @JsonIgnore
    lateinit var partitionKey: String
    @JsonIgnore
    lateinit var raw: String

    val data: String by lazy {
        values.toString()
    }

    override fun compareTo(other: RecordData): Int {
        return recordTime.compareTo(other.recordTime)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecordData

        if (seq != other.seq) return false
        if (shardId != other.shardId) return false
        if (partitionKey != other.partitionKey) return false

        return true
    }

    override fun hashCode(): Int {
        var result = seq.hashCode()
        result = 31 * result + shardId.hashCode()
        result = 31 * result + partitionKey.hashCode()
        return result
    }
}
