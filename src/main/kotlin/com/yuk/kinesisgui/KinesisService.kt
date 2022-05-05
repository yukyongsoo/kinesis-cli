package com.yuk.kinesisgui

import com.amazonaws.services.kinesis.AmazonKinesis
import com.amazonaws.services.kinesis.model.GetRecordsRequest
import com.amazonaws.services.kinesis.model.GetShardIteratorRequest
import com.amazonaws.services.kinesis.model.ListShardsRequest
import com.amazonaws.services.kinesis.model.PutRecordRequest
import com.amazonaws.services.kinesis.model.Record
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import org.springframework.stereotype.Service
import java.nio.ByteBuffer
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

@Service
class KinesisService(
    private val kinesisClient: AmazonKinesis
) {
    fun getStreamList(
        word: String
    ): List<String> {
        val list = kinesisClient.listStreams()

        return if (word.isNotEmpty()) list.streamNames.filter { it.contains(word) }
        else list.streamNames
    }

    fun getShardIds(
        streamName: String
    ): List<String> {
        val request = ListShardsRequest()
        request.streamName = streamName

        val response = kinesisClient.listShards(request)

        return response.shards.map { it.shardId }
    }

    fun getShardIterator(
        streamName: String,
        shardId: String,
        shardIteratorType: String
    ): String {
        val shardIteratorRequest = GetShardIteratorRequest()
        shardIteratorRequest.streamName = streamName
        shardIteratorRequest.shardId = shardId
        shardIteratorRequest.shardIteratorType = "LATEST"

        val shardIteratorResult =
            kinesisClient.getShardIterator(shardIteratorRequest)

        return shardIteratorResult.shardIterator
    }

    fun getRecords(
        shardIterator: String,
        limit: Int
    ): List<String> {
        val getRecordsRequest = GetRecordsRequest()
        getRecordsRequest.shardIterator = shardIterator
        getRecordsRequest.limit = limit

        val getRecordsResult = kinesisClient.getRecords(getRecordsRequest)
        return getRecordsResult.records.map { it.data.array().toString(Charsets.UTF_8) }
    }

    fun addRecord(
        streamName: String,
        data: String,
    ): String {
        val putRecordRequest = PutRecordRequest()
        putRecordRequest.streamName = streamName
        putRecordRequest.data = ByteBuffer.wrap(
            data.toByteArray()
        )
        putRecordRequest.partitionKey = "0"

        val putRecordResult = kinesisClient.putRecord(putRecordRequest)
        return "${putRecordResult.shardId} : ${putRecordResult.sequenceNumber}"
    }
}