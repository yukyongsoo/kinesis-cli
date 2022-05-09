package com.yuk.kinesisgui.processor

import java.io.FileWriter

class FileRecordProcessor : RecordProcessor {
    @Synchronized
    override fun processRecord(data: String) {
        FileWriter("kinesis.txt", true).use {
            it.write("$data\n")
        }
    }
}
