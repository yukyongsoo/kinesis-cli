package com.yuk.kinesisgui.gui

import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.UIScope
import com.yuk.kinesisgui.gui.event.EventGrid
import com.yuk.kinesisgui.metric.MetricClassifier
import com.yuk.kinesisgui.metric.MetricDataSet
import com.yuk.kinesisgui.stream.KinesisService
import com.yuk.kinesisgui.stream.StreamTracker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import java.time.LocalDateTime

@UIScope
@SpringComponent
class SessionContext(
    private val metricClassifier: MetricClassifier,
    private val kinesisService: KinesisService,
) {
    private var currentStreamName = ""
    private val streamTracker = StreamTracker(kinesisService)

    @Autowired
    @Lazy
    private lateinit var eventGrid: EventGrid

    fun changeCurrentStreamName(streamName: String) {
        currentStreamName = streamName
        stopTracking()
        startTracking()
    }

    fun getMetrics(): MetricDataSet {
        return metricClassifier.classify(currentStreamName)
    }

    fun getStreamList(): List<String> {
        return kinesisService.getStreamList()
    }

    fun startTracking() {
        streamTracker.start(currentStreamName, false, null)
        streamTracker.addRecordProcessor(eventGrid.gridRecordProcessor)
    }

    fun trimHorizon(value: Boolean) {
        stopTracking()
        streamTracker.start(currentStreamName, value, null)
    }

    fun afterTime(value: LocalDateTime) {
        stopTracking()
        streamTracker.start(currentStreamName, false, value)
    }

    fun stopTracking() {
        streamTracker.stop()
    }

    fun addRecord(record: String) {
        kinesisService.addRecord(currentStreamName, record)
    }
}
