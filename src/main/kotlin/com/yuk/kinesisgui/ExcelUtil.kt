package com.yuk.kinesisgui

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.yuk.kinesisgui.gui.EventData
import java.io.FileInputStream
import java.io.FileWriter

object ExcelUtil {
    private val csvMapper = CsvMapper().apply {
        disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
    }
    private var csvSchema = csvMapper.schemaFor(EventData::class.java)
        .withHeader()
        .withColumnSeparator('\t')
        .withLineSeparator("\t\n")

    fun createFile(data: Collection<EventData>) {
        val writer = csvMapper.writer(csvSchema)
        FileWriter("event.csv").use {
            writer.writeValues(it).writeAll(data)
        }
    }

    fun readFile(): ByteArray {
        FileInputStream("event.csv").use {
            return it.readBytes()
        }
    }
}