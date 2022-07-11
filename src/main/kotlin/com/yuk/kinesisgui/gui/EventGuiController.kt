package com.yuk.kinesisgui.gui

import com.yuk.kinesisgui.ExcelUtil
import com.yuk.kinesisgui.KinesisService
import com.yuk.kinesisgui.MonitoringService
import com.yuk.kinesisgui.StreamTrackerManager
import com.yuk.kinesisgui.processor.GridRecordProcessor
import java.time.LocalDateTime

object EventGuiController {
    private lateinit var kinesisService: KinesisService
    private lateinit var streamTrackerManager: StreamTrackerManager
    private lateinit var gridRecordProcessor: GridRecordProcessor
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

        if (streamName != CurrentState.streamName) {
            streamTrackerManager.removeRecordProcessor(CurrentState.streamName, gridRecordProcessor)
            streamTrackerManager.addRecordProcessor(streamName, gridRecordProcessor)
            CurrentState.streamName = streamName
        }
    }

    fun trimHorizon(trimHorizon: Boolean) {
        grid.clean()

        if (streamTrackerManager.isTracked(CurrentState.streamName))
            streamTrackerManager.stopTracking(CurrentState.streamName)

        streamTrackerManager.startTracking(CurrentState.streamName, trimHorizon)
        streamTrackerManager.addRecordProcessor(CurrentState.streamName, gridRecordProcessor)
    }

    fun stopTracking() {
        if (streamTrackerManager.isTracked(CurrentState.streamName))
            streamTrackerManager.stopTracking(CurrentState.streamName)
    }

    fun afterTime(afterTime: LocalDateTime) {
        grid.clean()

        if (streamTrackerManager.isTracked(CurrentState.streamName))
            streamTrackerManager.stopTracking(CurrentState.streamName)

        streamTrackerManager.startTracking(CurrentState.streamName, afterTime = afterTime)
        streamTrackerManager.addRecordProcessor(CurrentState.streamName, gridRecordProcessor)
    }

    fun addRecord(record: String) {
        kinesisService.addRecord(CurrentState.streamName, record)
    }

    fun exportExcel(): ByteArray {
        val list = grid.currentItems()
        ExcelUtil.createFile(list)
        return ExcelUtil.readFile()
    }

}
