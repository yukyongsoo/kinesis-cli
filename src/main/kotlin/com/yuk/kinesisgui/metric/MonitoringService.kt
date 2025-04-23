package com.yuk.kinesisgui.metric

import org.springframework.stereotype.Service
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient
import software.amazon.awssdk.services.cloudwatch.model.DimensionFilter
import software.amazon.awssdk.services.cloudwatch.model.GetMetricDataRequest
import software.amazon.awssdk.services.cloudwatch.model.ListMetricsRequest
import software.amazon.awssdk.services.cloudwatch.model.Metric
import software.amazon.awssdk.services.cloudwatch.model.MetricDataQuery
import software.amazon.awssdk.services.cloudwatch.model.MetricDataResult
import software.amazon.awssdk.services.cloudwatch.model.MetricStat
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID

@Service
class MonitoringService(
    private val client: CloudWatchClient,
) {
    fun getStreamMetricList(streamName: String): List<Metric> {
        var nextToken: String? = null

        val metricList =
            buildList<Metric> {
                do {
                    val listMetricsRequest =
                        ListMetricsRequest.builder()
                            .namespace("AWS/Kinesis")
                            .dimensions(
                                DimensionFilter.builder()
                                    .name("StreamName")
                                    .value(streamName)
                                    .build(),
                            )
                            .nextToken(nextToken)
                            .build()

                    val listMetricsResult = client.listMetrics(listMetricsRequest)

                    addAll(listMetricsResult.metrics())

                    nextToken = listMetricsResult.nextToken()
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
            GetMetricDataRequest
                .builder()
                .metricDataQueries(query)
                .startTime(startTimeInstant.toInstant())
                .endTime(endTimeInstant.toInstant())
                .build()

        val response = client.getMetricData(request)

        return response.metricDataResults()
    }

    private fun makeMetricStat(metric: Metric): MetricStat {
        val metricStat =
            MetricStat.builder()
                .metric(metric)
                .period(120)
                .stat("Sum")
                .build()

        return metricStat
    }

    private fun makeMetricDataQuery(metricStat: MetricStat): MetricDataQuery {
        val id = "a${UUID.randomUUID().toString().replace("-", "_")}"

        val query =
            MetricDataQuery.builder()
                .metricStat(metricStat)
                .id(id)
                .build()

        return query
    }
}
