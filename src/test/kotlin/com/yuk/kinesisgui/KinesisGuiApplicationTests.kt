package com.yuk.kinesisgui

import com.yuk.kinesisgui.metric.MetricClassifier
import com.yuk.kinesisgui.stream.KinesisService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class KinesisGuiApplicationTests {
    @Autowired
    private lateinit var kinesisService: KinesisService

    @Autowired
    private lateinit var metricClassifier: MetricClassifier

    @Test
    fun `스트림 리스트 가져오기`() {
        kinesisService.getStreamList()
    }

    @Test
    fun `메트릭 리스트 가져오기`() {
        val data = metricClassifier.classify("bun-product-inspection-event-prod")

        println(data)
    }
}
