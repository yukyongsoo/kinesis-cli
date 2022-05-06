package com.yuk.kinesisgui

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.stereotype.Service

@Service
@ConditionalOnMissingBean(RecordProcessor::class)
class ConsoleRecordProcessor : RecordProcessor {
    override fun processRecord(data: String) {
        println(data)
    }
}
