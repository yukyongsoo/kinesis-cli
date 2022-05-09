package com.yuk.kinesisgui

import com.amazonaws.services.kinesis.AmazonKinesis
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.shared.communication.PushMode
import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.shell.jline.PromptProvider

@Configuration(proxyBeanMethods = false)
@Push(PushMode.AUTOMATIC)
class Config : AppShellConfigurator {
    companion object {
        val objectMapper = JsonMapper.builder()
            .addModule(KotlinModule())
            .findAndAddModules()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build()!!
    }

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

    @Bean
    fun getObjectMapper(): ObjectMapper {
        return objectMapper
    }
}
