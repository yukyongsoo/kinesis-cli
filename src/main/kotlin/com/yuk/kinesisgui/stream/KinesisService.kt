package com.yuk.kinesisgui.stream

import org.springframework.stereotype.Service
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.kinesis.KinesisClient
import software.amazon.awssdk.services.kinesis.model.GetRecordsRequest
import software.amazon.awssdk.services.kinesis.model.GetRecordsResponse
import software.amazon.awssdk.services.kinesis.model.GetShardIteratorRequest
import software.amazon.awssdk.services.kinesis.model.ListShardsRequest
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest
import java.nio.charset.Charset
import java.util.Date

@Service
class KinesisService(
    private val kinesisClient: KinesisClient,
) {
    fun getStreamList(word: String = ""): List<String> {
        val list = kinesisClient.listStreams()

        list.streamNames()

        return if (word.isNotEmpty()) {
            list.streamNames().filter { it.contains(word) }
        } else {
            list.streamNames()
        }
    }

    fun getShardIds(streamName: String): List<String> {
        val request =
            ListShardsRequest.builder()
                .streamName(streamName)
                .build()

        val response = kinesisClient.listShards(request)

        return response.shards().map { it.shardId() }
    }

    fun getShardIterator(
        streamName: String,
        shardId: String,
        shardIteratorType: String = "LATEST",
        startDate: Date = Date(),
    ): String {
        val shardIteratorRequest =
            GetShardIteratorRequest.builder()
                .streamName(streamName)
                .shardId(shardId)
                .shardIteratorType(shardIteratorType)

        if (shardIteratorType == "AT_TIMESTAMP") {
            shardIteratorRequest
                .timestamp(startDate.toInstant())
        }

        val shardIteratorResult =
            kinesisClient.getShardIterator(shardIteratorRequest.build())

        return shardIteratorResult.shardIterator()
    }

    fun getRecords(
        shardIterator: String,
        limit: Int,
    ): GetRecordsResponse {
        val getRecordsRequest =
            GetRecordsRequest
                .builder()
                .shardIterator(shardIterator)
                .limit(limit)
                .build()

        return kinesisClient.getRecords(getRecordsRequest)
    }

    fun addRecord(
        streamName: String,
        data: String,
    ): String {
        val putRecordRequest =
            PutRecordRequest
                .builder()
                .streamName(streamName)
                .data(
                    SdkBytes.fromString(
                        data,
                        Charset.defaultCharset(),
                    ),
                )
                .partitionKey("0")
                .build()

        val putRecordResult = kinesisClient.putRecord(putRecordRequest)
        return "${putRecordResult.shardId()} : ${putRecordResult.sequenceNumber()}"
    }
}
