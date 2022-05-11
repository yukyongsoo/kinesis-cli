package com.yuk.kinesisgui.processor

import com.yuk.kinesisgui.RecordData

class ConsoleRecordProcessor : RecordProcessor {
    override fun processRecord(records: List<RecordData>) {
        records.forEach {
            println(it.data)
        }
    }
}
