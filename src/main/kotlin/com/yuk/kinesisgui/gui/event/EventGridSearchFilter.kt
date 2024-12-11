package com.yuk.kinesisgui.gui.event

import com.yuk.kinesisgui.stream.RecordData
import java.util.Locale

class EventGridSearchFilter {
    var shardId = ""
    var seq = ""
    var recordTime = ""
    var data = ""

    fun filter(searchData: RecordData): Boolean {
        val matchShardId = matches(searchData.shardId, shardId)
        val matchSeq = matches(searchData.seq, seq)
        val matchRecordTime = matches(searchData.recordTime, recordTime)
        val multiMatchData = multiMatches(searchData.data, data)

        return matchRecordTime && matchSeq && matchShardId && multiMatchData
    }

    private fun matches(
        value: String,
        searchTerm: String,
    ): Boolean {
        return searchTerm.isEmpty() ||
            value.lowercase(Locale.getDefault()).contains(searchTerm.lowercase(Locale.getDefault()))
    }

    private fun multiMatches(
        value: String,
        searchTerm: String,
    ): Boolean {
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
