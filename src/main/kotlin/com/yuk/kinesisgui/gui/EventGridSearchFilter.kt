package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.grid.dataview.GridListDataView
import java.util.Locale

class EventGridSearchFilter(
    private val dataView: GridListDataView<EventData>
) {
    var eventTime = ""
        set(value) {
            field = value
            dataView.refreshAll()
        }
    var eventType = ""
        set(value) {
            field = value
            dataView.refreshAll()
        }
    var source = ""
        set(value) {
            field = value
            dataView.refreshAll()
        }
    var data = ""
        set(value) {
            field = value
            dataView.refreshAll()
        }

    init {
        dataView.addFilter(this::filter)
    }

    fun filter(searchData: EventData): Boolean {
        val matchTime = matches(searchData.eventTime, eventTime)
        val matchType = matches(searchData.eventType, eventType)
        val matchSource = matches(searchData.source, source)
        val matchData = matches(searchData.data.toString(), data)

        return matchTime && matchType && matchSource && matchData
    }

    private fun matches(value: String, searchTerm: String?): Boolean {
        return searchTerm == null || searchTerm.isEmpty() || value.lowercase(
            Locale.getDefault()
        )
            .contains(searchTerm.lowercase(Locale.getDefault()))
    }
}
