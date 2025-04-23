package com.yuk.kinesisgui

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
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient
import software.amazon.awssdk.services.kinesis.KinesisClient

@Configuration(proxyBeanMethods = false)
@Push(PushMode.AUTOMATIC)
class Config : AppShellConfigurator {
    private val region = Region.AP_NORTHEAST_2

    companion object {
        val objectMapper =
            JsonMapper.builder()
                .addModule(KotlinModule.Builder().build())
                .findAndAddModules()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build()!!
    }

    @Bean
    fun getObjectMapper(): ObjectMapper {
        return objectMapper
    }

    @Bean
    fun getKinesisOperator(
        @Value("\${aws.profile:default}") profileName: String,
    ): KinesisClient {
        val builder =
            KinesisClient
                .builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create(profileName))

        return builder.build()
    }

    @Bean
    fun cloudWatchOperator(
        @Value("\${aws.profile:default}") profileName: String,
    ): CloudWatchClient {
        val builder =
            CloudWatchClient.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create(profileName))

        return builder.build()
    }

//    @Bean
//    fun stsOperator(
//        @Value("\${aws.profile:default}") profileName: String,
//    ) {
//    }
}
