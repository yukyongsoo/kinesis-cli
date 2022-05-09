package com.yuk.kinesisgui.processor

class ConsoleRecordProcessor : RecordProcessor {
    override fun processRecord(data: String) {
        println(data)
    }
}
