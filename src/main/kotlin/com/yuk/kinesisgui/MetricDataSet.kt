package com.yuk.kinesisgui

import com.amazonaws.services.cloudwatch.model.MetricDataResult
import java.sql.Timestamp
import java.time.LocalDateTime

class MetricDataSet {
    val putRecordsTotal = mutableListOf<Pair<LocalDateTime,Double>>()
    val putRecordsByte = mutableListOf<Pair<LocalDateTime,Double>>()
    val putRecordLatency = mutableListOf<Pair<LocalDateTime,Double>>()
    val putRecordThrottled = mutableListOf<Pair<LocalDateTime,Double>>()
    val putRecordsSuccess = mutableListOf<Pair<LocalDateTime,Double>>()
    val putRecordsFailed = mutableListOf<Pair<LocalDateTime,Double>>()
    val incomingRecords = mutableListOf<Pair<LocalDateTime,Double>>()
    val subscribeToShardEventMillisBehindLatest = mutableListOf<Pair<LocalDateTime,Double>>()
    val subscribeToShardEventSuccess = mutableListOf<Pair<LocalDateTime,Double>>()
    val subscribeToShardRecord = mutableListOf<Pair<LocalDateTime,Double>>()
    val subscribeToShardRateExceeded = mutableListOf<Pair<LocalDateTime,Double>>()
    val writeProvisionedThroughputExceeded = mutableListOf<Pair<LocalDateTime,Double>>()

    fun addAll(metricDataResults: List<MetricDataResult>) {
        metricDataResults.forEach {result ->
            val timeToValue = result.timestamps.zip(result.values)

            timeToValue.forEach {(date, value) ->
                val localDateTime = Timestamp(date.time).toLocalDateTime()

                when (result.label) {
                    "PutRecords.TotalRecords" -> putRecordsTotal.add(Pair(localDateTime, value))
                    "PutRecords.Bytes" -> putRecordsByte.add(Pair(localDateTime, value))
                    "PutRecords.Latency" -> putRecordLatency.add(Pair(localDateTime, value))
                    "PutRecords.ThrottledRecords" -> putRecordThrottled.add(Pair(localDateTime, value))
                    "PutRecords.SuccessfulRecords" -> putRecordsSuccess.add(Pair(localDateTime, value))
                    "PutRecords.FailedRecords" -> putRecordsFailed.add(Pair(localDateTime, value))
                    "IncomingRecords" -> incomingRecords.add(Pair(localDateTime, value))
                    "SubscribeToShardEvent.MillisBehindLatest" -> subscribeToShardEventMillisBehindLatest.add(Pair(localDateTime, value))
                    "SubscribeToShardEvent.Success" -> subscribeToShardEventSuccess.add(Pair(localDateTime, value))
                    "SubscribeToShardEvent.Records" -> subscribeToShardRecord.add(Pair(localDateTime, value))
                    "SubscribeToShard.RateExceeded" -> subscribeToShardRateExceeded.add(Pair(localDateTime, value))
                    "WriteProvisionedThroughputExceeded" -> writeProvisionedThroughputExceeded.add(Pair(localDateTime, value))
                }
            }
        }
    }

    fun sortAll() {
        putRecordsTotal.sortBy { it.first }
        putRecordsByte.sortBy { it.first }
        putRecordLatency.sortBy { it.first }
        putRecordThrottled.sortBy { it.first }
        putRecordsSuccess.sortBy { it.first }
        putRecordsFailed.sortBy { it.first }
        incomingRecords.sortBy { it.first }
        subscribeToShardEventMillisBehindLatest.sortBy { it.first }
        subscribeToShardEventSuccess.sortBy { it.first }
        subscribeToShardRecord.sortBy { it.first }
        subscribeToShardRateExceeded.sortBy { it.first }
        writeProvisionedThroughputExceeded.sortBy { it.first }
    }
}
