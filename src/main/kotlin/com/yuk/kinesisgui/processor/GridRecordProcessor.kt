package com.yuk.kinesisgui.processor

import com.yuk.kinesisgui.RecordData
import com.yuk.kinesisgui.gui.EventGrid

class GridRecordProcessor(
    private val eventGrid: EventGrid
) : RecordProcessor {
    override fun processRecord(records: List<RecordData>) {
        eventGrid.addItems(records)
    }
}
