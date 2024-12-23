package com.yuk.kinesisgui.gui.event

import com.yuk.kinesisgui.stream.RecordData
import com.yuk.kinesisgui.stream.RecordProcessor

class ToolbarRecordProcessor(
    private val toolbar: Toolbar,
) : RecordProcessor {
    override fun processRecord(
        records: Collection<RecordData>,
        iterator: String,
    ) {
        if (toolbar.isAttached) {
            toolbar.setSpinnerTooltip(iterator)
        }
    }
}
