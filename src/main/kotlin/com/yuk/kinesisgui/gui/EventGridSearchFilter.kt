package com.yuk.kinesisgui.gui

import java.util.Locale

class EventGridSearchFilter {
    var eventTime = ""
    var eventType = ""
    var source = ""
    var data = ""

    fun filter(searchData: EventData): Boolean {
        val matchTime = matches(searchData.eventTime, eventTime)
        val matchType = matches(searchData.eventType, eventType)
        val matchSource = matches(searchData.source, source)
        val matchData = multiMatches(searchData.data, data)

        return matchTime && matchType && matchSource && matchData
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
