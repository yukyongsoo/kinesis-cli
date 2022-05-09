package com.yuk.kinesisgui

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication
class KinesisGuiApplication : SpringBootServletInitializer()

fun main(args: Array<String>) {
    runApplication<KinesisGuiApplication>(*args)
}
