package com.yuk.kinesisgui

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KinesisGuiApplication

fun main(args: Array<String>) {
    System.setProperty("spring.profiles.active", "cli")
    runApplication<KinesisGuiApplication>(*args)
}
