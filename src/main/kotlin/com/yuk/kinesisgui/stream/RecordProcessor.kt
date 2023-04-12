package com.yuk.kinesisgui.stream

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
