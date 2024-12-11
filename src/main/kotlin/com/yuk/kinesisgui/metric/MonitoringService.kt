package com.yuk.kinesisgui.metric

import com.amazonaws.services.cloudwatch.AmazonCloudWatch
import com.amazonaws.services.cloudwatch.model.DimensionFilter
import com.amazonaws.services.cloudwatch.model.GetMetricDataRequest
import com.amazonaws.services.cloudwatch.model.ListMetricsRequest
import com.amazonaws.services.cloudwatch.model.Metric
import com.amazonaws.services.cloudwatch.model.MetricDataQuery
import com.amazonaws.services.cloudwatch.model.MetricDataResult
import com.amazonaws.services.cloudwatch.model.MetricStat
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID

@Service
class MonitoringService(
    private val client: AmazonCloudWatch,
) {
    fun getStreamMetricList(streamName: String): List<Metric> {
        var nextToken: String? = null

        val metricList =
            buildList<Metric> {
                do {
                    val listMetricsRequest =
                        ListMetricsRequest().withNamespace("AWS/Kinesis")
                            .withDimensions(DimensionFilter().withName("StreamName").withValue(streamName))
                            .withNextToken(nextToken)

                    val listMetricsResult = client.listMetrics(listMetricsRequest)

                    addAll(listMetricsResult.metrics)

                    nextToken = listMetricsResult.nextToken
                } while (nextToken?.isNotBlank() == true)
            }

        return metricList
    }

    fun getMetric(
        metric: Metric,
        startDateTime: LocalDateTime,
    ): MutableList<MetricDataResult> {
        val metricStat = makeMetricStat(metric)

        val query = makeMetricDataQuery(metricStat)

        val currentDatetime = LocalDateTime.now()
        val startTimeInstant = Timestamp.valueOf(startDateTime)
        val endTimeInstant = Timestamp.valueOf(currentDatetime)

        val request =
            GetMetricDataRequest()
                .withMetricDataQueries(query)
                .withStartTime(startTimeInstant)
                .withEndTime(endTimeInstant)

        val response = client.getMetricData(request)

        return response.metricDataResults
    }

    private fun makeMetricStat(metric: Metric): MetricStat {
        val metricStat = MetricStat().withMetric(metric).withPeriod(120).withStat("Sum")

        return metricStat
    }

    private fun makeMetricDataQuery(metricStat: MetricStat): MetricDataQuery {
        val id = "a${UUID.randomUUID().toString().replace("-", "_")}"

        val query = MetricDataQuery().withMetricStat(metricStat).withId(id)

        return query
    }
}
