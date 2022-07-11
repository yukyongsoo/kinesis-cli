package com.yuk.kinesisgui

import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MetricClassifier(
    private val monitoringService: MonitoringService
) {
    fun classify(streamName: String): MetricDataSet {
        val metrics = monitoringService.getStreamMetricList(streamName)
        val dataset = MetricDataSet()

        val results = metrics.map { metric ->
            monitoringService.getMetric(metric, LocalDateTime.now().minusHours(6))
        }

        results.forEach {
            dataset.addAll(it)
        }

        return dataset
    }

}