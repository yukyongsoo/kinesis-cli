package com.yuk.kinesisgui.gui

import com.yuk.kinesisgui.ExcelUtil
import com.yuk.kinesisgui.KinesisService
import com.yuk.kinesisgui.StreamTrackerManager
import com.yuk.kinesisgui.processor.GridRecordProcessor

object GuiController {
    private lateinit var kinesisService: KinesisService
    private lateinit var streamTrackerManager: StreamTrackerManager
    private lateinit var gridRecordProcessor: GridRecordProcessor
    private var currentStreamName = ""
    private lateinit var grid: EventGrid

    fun setKinesisService(kinesisService: KinesisService) {
        this.kinesisService = kinesisService
    }

    fun setStreamTrackerManager(streamTrackerManager: StreamTrackerManager) {
        this.streamTrackerManager = streamTrackerManager
    }

    fun setGrid(eventGrid: EventGrid) {
        grid = eventGrid
        gridRecordProcessor = GridRecordProcessor(eventGrid)
    }

    fun getStreamList(): List<String> {
        return kinesisService.getStreamList()
    }

    fun selectedStream(streamName: String) {
        grid.clean()

        if (streamTrackerManager.isTracked(streamName).not()) {
            streamTrackerManager.startTracking(streamName)
        }

        if (streamName != currentStreamName) {
            streamTrackerManager.removeRecordProcessor(currentStreamName, gridRecordProcessor)
            streamTrackerManager.addRecordProcessor(streamName, gridRecordProcessor)
            currentStreamName = streamName
        }
    }

    fun trimHorizon(trimHorizon: Boolean) {
        grid.clean()

        if (streamTrackerManager.isTracked(currentStreamName))
            streamTrackerManager.stopTracking(currentStreamName)

        streamTrackerManager.startTracking(currentStreamName, trimHorizon)
        streamTrackerManager.addRecordProcessor(currentStreamName, gridRecordProcessor)
    }

    fun addRecord(record: String) {
        kinesisService.addRecord(currentStreamName, record)
    }

    fun exportExcel(): ByteArray {
        val list = grid.currentItems()
        ExcelUtil.createFile(list)
        return ExcelUtil.readFile()
    }
}
