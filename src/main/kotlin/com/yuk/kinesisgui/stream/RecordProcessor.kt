package com.yuk.kinesisgui.stream

interface RecordProcessor {
    fun processRecord(
        records: Collection<RecordData>,
        iterator: String,
    )

    fun process(
        records: Collection<RecordData>,
        iterator: String,
    ) {
        try {
            processRecord(records, iterator)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
