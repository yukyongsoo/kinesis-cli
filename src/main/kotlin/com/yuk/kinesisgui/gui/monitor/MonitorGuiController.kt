package com.yuk.kinesisgui.gui.monitor

import com.yuk.kinesisgui.gui.CurrentState
import com.yuk.kinesisgui.metric.MetricClassifier

object MonitorGuiController {
    private lateinit var metricClassifier: MetricClassifier
    private lateinit var monitorView: MonitorView

    fun setMonitorView(monitorView: MonitorView) {
        MonitorGuiController.monitorView = monitorView
    }

    fun setMonitorService(metricClassifier: MetricClassifier) {
        MonitorGuiController.metricClassifier = metricClassifier
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
