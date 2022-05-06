package com.yuk.kinesisgui

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption

@ShellComponent
class ShellService(
    private val kinesisService: KinesisService,
    private val streamTrackerManager: StreamTrackerManager
) {
    @ShellMethod(value = "스트림 목록 조회", key = ["streams"])
    fun getStreamList(
        @ShellOption(defaultValue = "") word: String
    ): List<String> {
        return kinesisService.getStreamList(word)
    }

    @ShellMethod(value = "스트림의 샤드 목록 조회", key = ["shards"])
    fun getShardIds(
        @ShellOption streamName: String
    ): List<String> {
        return kinesisService.getShardIds(streamName)
    }

    @ShellMethod(value = "샤드 반복자 조회", key = ["shard"])
    fun getShardIterator(
        @ShellOption streamName: String,
        @ShellOption shardId: String,
        @ShellOption(defaultValue = "LATEST")
        shardIteratorType: String
    ): String {
        return kinesisService.getShardIterator(streamName, shardId, shardIteratorType)
    }

    @ShellMethod(value = "레코드 가져오기(10000개 제한)", key = ["get"])
    fun getRecords(
        @ShellOption shardIterator: String,
        @ShellOption(defaultValue = "25") limit: Int
    ): List<String> {
        val result = kinesisService.getRecords(shardIterator, limit)

        return result.records.map { it.data.toString() }
    }

    @ShellMethod(value = "레코드 입력", key = ["add"])
    fun addRecord(
        @ShellOption streamName: String,
        @ShellOption data: String,
    ): String {
        return kinesisService.addRecord(streamName, data)
    }

    @ShellMethod(value = "스트림 추적", key = ["track"])
    fun trackStream(
        @ShellOption streamName: String,
    ) {
        streamTrackerManager.startTracking(streamName)
    }

    @ShellMethod(value = "스트림 추적 중지", key = ["untrack"])
    fun untrackStream(
        @ShellOption streamName: String
    ){
        streamTrackerManager.stopTracking(streamName)
    }
}
