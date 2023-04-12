package com.yuk.kinesisgui.processor

import com.yuk.kinesisgui.stream.RecordData

interface RecordProcessor {
    fun processRecord(records: Collection<RecordData>)

    fun process(records: Collection<RecordData>) {
        try {
            processRecord(records)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
