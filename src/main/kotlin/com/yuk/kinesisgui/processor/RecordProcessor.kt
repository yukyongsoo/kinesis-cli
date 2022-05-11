package com.yuk.kinesisgui.processor

import com.yuk.kinesisgui.RecordData

interface RecordProcessor {
    fun processRecord(records: List<RecordData>)

    fun process(records: List<RecordData>) {
        try {
            processRecord(records)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
