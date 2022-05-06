package com.yuk.kinesisgui

import org.springframework.stereotype.Service

@Service
class ConsoleRecordProcessor : RecordProcessor {
    override fun processRecord(data: String) {
        println(data)
    }
}
