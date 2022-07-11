package com.yuk.kinesisgui.gui

import com.yuk.kinesisgui.MetricClassifier

object MonitorGuiController {
    private lateinit var metricClassifier: MetricClassifier
    private lateinit var monitorView: MonitorView

    fun setMonitorView(monitorView: MonitorView) {
        this.monitorView = monitorView
    }

    fun setMonitorService(metricClassifier: MetricClassifier) {
        this.metricClassifier = metricClassifier
    }

    fun setMetric() {
        val metrics = metricClassifier.classify(CurrentState.streamName)
        metrics.sortAll()

        monitorView.setChart(metrics)
    }

    fun selectedStream(streamName: String) {
        monitorView.clear()
        setMetric()
    }
}