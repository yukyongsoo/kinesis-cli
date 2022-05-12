package com.yuk.kinesisgui.gui

import com.yuk.kinesisgui.RecordData
import java.util.Locale

class EventGridSearchFilter {
    var shardId = ""
    var partitionKey = ""
    var seq = ""
    var recordTime = ""
    var eventTime = ""
    var eventType = ""
    var source = ""
    var data = ""

    fun filter(searchData: RecordData): Boolean {
        val matchShardId = matches(searchData.shardId, shardId)
        val matchPartitionKey = matches(searchData.partitionKey, partitionKey)
        val matchSeq = matches(searchData.seq, seq)
        val matchRecordTime = matches(searchData.recordTime, recordTime)
        val matchTime = matches(searchData.eventTime, eventTime)
        val matchType = matches(searchData.eventType, eventType)
        val matchSource = matches(searchData.source, source)
        val matchData = multiMatches(searchData.data, data)

        return matchRecordTime && matchTime && matchType && matchSource && matchData && matchSeq && matchShardId && matchPartitionKey
    }

    private fun matches(value: String, searchTerm: String): Boolean {
        return searchTerm.isEmpty() ||
            value.lowercase(Locale.getDefault()).contains(searchTerm.lowercase(Locale.getDefault()))
    }

    private fun multiMatches(value: String, searchTerm: String): Boolean {
        return when {
            searchTerm.contains("&") -> {
                val andTerms = searchTerm.split("&").filter { it.isNotEmpty() }
                andTerms.all { matches(value, it) }
            }
            searchTerm.contains("|") -> {
                val orTerms = searchTerm.split("|").filter { it.isNotEmpty() }
                orTerms.any { matches(value, it) }
            }
            else -> matches(value, searchTerm)
        }
    }
}
