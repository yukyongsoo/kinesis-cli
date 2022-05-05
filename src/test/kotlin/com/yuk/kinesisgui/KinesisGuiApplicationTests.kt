package com.yuk.kinesisgui

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class KinesisGuiApplicationTests {
    @Autowired
    private lateinit var kinesisService: KinesisService

    @Test
    fun `스트림 리스트 가져오기`() {
        kinesisService.getStreamList()
    }
}

