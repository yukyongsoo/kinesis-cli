package com.yuk.kinesisgui.processor

import com.yuk.kinesisgui.RecordData
import java.io.FileWriter

class FileRecordProcessor : RecordProcessor {
    @Synchronized
    override fun processRecord(records: List<RecordData>) {
        FileWriter("kinesis.txt", true).use { writer ->
            records.forEach {
                writer.write("${it.data}\n")
            }
        }
    }
}
