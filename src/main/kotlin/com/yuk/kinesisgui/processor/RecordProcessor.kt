package com.yuk.kinesisgui.processor

interface RecordProcessor {
    fun processRecord(data: String)

    fun process(data: String) {
        try {
            processRecord(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
