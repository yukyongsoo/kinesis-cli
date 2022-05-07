package com.yuk.kinesisgui

import org.springframework.stereotype.Service
import java.io.FileWriter

@Service
class FileRecordProcessor : RecordProcessor {
    @Synchronized
    override fun processRecord(data: String) {
        FileWriter("kinesis.txt", true).use {
            it.write("$data\n")
        }
    }
}
