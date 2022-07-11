package com.yuk.kinesisgui

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.shell.jline.InteractiveShellApplicationRunner
import org.springframework.shell.jline.ScriptShellApplicationRunner
import java.time.LocalDateTime

@SpringBootTest(properties = [
    InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
    ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
])
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

        data.sortAll()
        println(data)
    }
}
