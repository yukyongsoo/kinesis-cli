package com.yuk.kinesisgui.gui

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
        if (streamTrackerManager.isTracked(streamName).not()) {
            streamTrackerManager.startTracking(streamName)
        }

        if (streamName != currentStreamName) {
            grid.clean()

            streamTrackerManager.removeRecordProcessor(currentStreamName, gridRecordProcessor)
            streamTrackerManager.addRecordProcessor(streamName, gridRecordProcessor)
            currentStreamName = streamName
        }
    }

    fun addRecord(record: String) {
        kinesisService.addRecord(currentStreamName, record)
    }
}
