package com.yuk.kinesisgui.gui.event

import com.yuk.kinesisgui.stream.RecordData
import com.yuk.kinesisgui.stream.RecordProcessor

class GridRecordProcessor(
    private val eventGrid: EventGrid,
) : RecordProcessor {
    override fun processRecord(records: Collection<RecordData>) {
        if (eventGrid.isAttached && records.isNotEmpty()) {
            eventGrid.addItems(records)
        }
    }
}
