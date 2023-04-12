package com.yuk.kinesisgui.gui

import com.yuk.kinesisgui.metric.MetricClassifier

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
        if (this::monitorView.isInitialized && monitorView.isAttached) {
            val metrics = metricClassifier.classify(CurrentState.streamName)
            metrics.sortAll()

            monitorView.clear()
            monitorView.setChart(metrics)
        }
    }
}
