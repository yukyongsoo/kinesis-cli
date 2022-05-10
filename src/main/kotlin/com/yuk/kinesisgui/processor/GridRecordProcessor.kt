package com.yuk.kinesisgui.processor

import com.fasterxml.jackson.module.kotlin.readValue
import com.yuk.kinesisgui.Config
import com.yuk.kinesisgui.gui.EventData
import com.yuk.kinesisgui.gui.EventGrid

class GridRecordProcessor(
    private val eventGrid: EventGrid
) : RecordProcessor {
    override fun processRecord(data: String) {
        val list = data.split("\n").filter { it.isNotBlank() }.map {
            Config.objectMapper.readValue<EventData>(it)
        }

        eventGrid.addItems(list.sortedBy { it.eventTime })
    }
}
