package com.yuk.kinesisgui

import com.amazonaws.services.kinesis.AmazonKinesis
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder
import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.shell.jline.PromptProvider

@Configuration
class Config {
    private val region = "ap-northeast-2"

    @Bean
    fun getKinesisOperator(): AmazonKinesis {
        val clientBuilder = AmazonKinesisClientBuilder.standard()
        clientBuilder.region = region

        return clientBuilder.build()
    }

    @Bean
    fun myPromptProvider(): PromptProvider {
        return PromptProvider {
            AttributedString(
                "kinesis:>",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)
            )
        }
    }
}
