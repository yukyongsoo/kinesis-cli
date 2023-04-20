package com.yuk.kinesisgui

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.cloudwatch.AmazonCloudWatch
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder
import com.amazonaws.services.kinesis.AmazonKinesis
import com.amazonaws.services.kinesis.AmazonKinesisAsyncClientBuilder
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.shared.communication.PushMode
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@Push(PushMode.AUTOMATIC)
class Config : AppShellConfigurator {
    private val region = "ap-northeast-2"

    companion object {
        val objectMapper = JsonMapper.builder()
            .addModule(KotlinModule())
            .findAndAddModules()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build()!!
    }

    @Bean
    fun getObjectMapper(): ObjectMapper {
        return objectMapper
    }

    @Bean
    fun getKinesisOperator(@Value("\${aws.profile:default}") profileName: String): AmazonKinesis {
        val builder = AmazonKinesisAsyncClientBuilder.standard()

        builder.region = region
        builder.withCredentials(ProfileCredentialsProvider(profileName))

        return builder.build()
    }

    @Bean
    fun cloudWatchOperator(@Value("\${aws.profile:default}") profileName: String): AmazonCloudWatch {
        val builder = AmazonCloudWatchClientBuilder.standard()

        builder.region = region
        builder.withCredentials(ProfileCredentialsProvider(profileName))

        return builder.build()
    }
}
