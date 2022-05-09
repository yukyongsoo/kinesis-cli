package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs

class StreamView : Tabs() {
    init {
        orientation = Orientation.VERTICAL

        val list = GuiController.getStreamList()
        list.sorted().forEach {
            add(Tab(it))
        }

        addSelectedChangeListener { event ->
            GuiController.selectedStream(event.selectedTab.label)
        }
    }
}
