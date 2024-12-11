package com.yuk.kinesisgui.metric

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MetricClassifier(
    private val monitoringService: MonitoringService,
) {
    fun classify(streamName: String): MetricDataSet {
        val metrics = monitoringService.getStreamMetricList(streamName)
        val dataset = MetricDataSet()

        val results =
            runBlocking {
                val deferred =
                    metrics.map { metric ->
                        async { monitoringService.getMetric(metric, LocalDateTime.now().minusHours(6)) }
                    }

                deferred.awaitAll()
            }

        results.forEach {
            dataset.addAll(it)
        }

        return dataset
    }
}
