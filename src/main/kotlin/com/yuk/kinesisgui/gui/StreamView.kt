package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.yuk.kinesisgui.KinesisService

class StreamView(
    private val kinesisService: KinesisService
) : Tabs() {
    init {
        orientation = Orientation.VERTICAL

        val list = kinesisService.getStreamList()
        list.forEach {
            add(Tab(it))
        }
    }
}
