package com.yuk.kinesisgui.gui.stream

import com.yuk.kinesisgui.stream.RecordData
import com.yuk.kinesisgui.stream.RecordProcessor

class GridRecordProcessor(
    private val eventGrid: EventGrid
) : RecordProcessor {
    override fun processRecord(records: Collection<RecordData>) {
        eventGrid.addItems(records)
    }
}
