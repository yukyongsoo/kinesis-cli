package com.yuk.kinesisgui.processor

import com.yuk.kinesisgui.RecordData

class ConsoleRecordProcessor : RecordProcessor {
    override fun processRecord(records: Collection<RecordData>) {
        records.forEach {
            println(it.data)
        }
    }
}
