package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs

class StreamView : Tabs() {
    init {
        orientation = Orientation.VERTICAL

        val list = EventGuiController.getStreamList()
        list.sorted().forEach {
            add(Tab(it))
        }

        CurrentState.streamName = list.first()

        addSelectedChangeListener { event ->
            CurrentState.streamName = event.selectedTab.label
            EventGuiController.selectedStream(event.selectedTab.label)
            MonitorGuiController.selectedStream(event.selectedTab.label)
        }
    }
}
