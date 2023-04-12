package com.yuk.kinesisgui.gui

import com.yuk.kinesisgui.ExcelUtil
import com.yuk.kinesisgui.gui.stream.EventGrid
import com.yuk.kinesisgui.gui.stream.GridRecordProcessor
import com.yuk.kinesisgui.stream.KinesisService
import com.yuk.kinesisgui.stream.StreamTrackerManager
import java.time.LocalDateTime

object EventGuiController {
    private lateinit var kinesisService: KinesisService
    private lateinit var streamTrackerManager: StreamTrackerManager
    private lateinit var gridRecordProcessor: GridRecordProcessor
    private lateinit var grid: EventGrid
    private var currentTrackStreamName = ""

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

    fun selectedStream() {
        if (this::grid.isInitialized && grid.isAttached) {
            grid.clean()

            if (streamTrackerManager.isTracked(CurrentState.streamName).not()) {
                streamTrackerManager.startTracking(CurrentState.streamName)
            }

            if (currentTrackStreamName != CurrentState.streamName) {
                streamTrackerManager.removeRecordProcessor(currentTrackStreamName, gridRecordProcessor)
                streamTrackerManager.addRecordProcessor(CurrentState.streamName, gridRecordProcessor)
            }

            currentTrackStreamName = CurrentState.streamName
        }
    }

    fun trimHorizon(trimHorizon: Boolean) {
        if (this::grid.isInitialized && grid.isAttached) {
            grid.clean()

            if (streamTrackerManager.isTracked(CurrentState.streamName))
                streamTrackerManager.stopTracking(CurrentState.streamName)

            streamTrackerManager.startTracking(CurrentState.streamName, trimHorizon)
            streamTrackerManager.addRecordProcessor(CurrentState.streamName, gridRecordProcessor)
        }
    }

    fun stopTracking() {
        if (streamTrackerManager.isTracked(CurrentState.streamName))
            streamTrackerManager.stopTracking(CurrentState.streamName)
    }

    fun afterTime(afterTime: LocalDateTime) {
        if (this::grid.isInitialized && grid.isAttached) {
            grid.clean()

            if (streamTrackerManager.isTracked(CurrentState.streamName))
                streamTrackerManager.stopTracking(CurrentState.streamName)

            streamTrackerManager.startTracking(CurrentState.streamName, afterTime = afterTime)
            streamTrackerManager.addRecordProcessor(CurrentState.streamName, gridRecordProcessor)
        }
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
