package com.yuk.kinesisgui.processor

import com.fasterxml.jackson.module.kotlin.readValue
import com.yuk.kinesisgui.Config
import com.yuk.kinesisgui.gui.EventData
import com.yuk.kinesisgui.gui.EventGrid

class GridRecordProcessor(
    private val eventGrid: EventGrid
) : RecordProcessor {
    override fun processRecord(data: String) {
        val list = data.split("\n").mapNotNull { json ->
            return@mapNotNull kotlin.runCatching {
                Config.objectMapper.readValue<EventData>(json)
            }.onSuccess { it }.getOrNull()
        }

        eventGrid.addItems(list.sortedBy { it.eventTime })
    }
}
