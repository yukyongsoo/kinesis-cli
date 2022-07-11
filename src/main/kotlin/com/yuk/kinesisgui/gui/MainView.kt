package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs

class MainView: VerticalLayout() {
    init {
        val eventViewTab = Tab("Event View")
        val monitorViewTab = Tab("Monitor View")

        val contentTab = Tabs()
        contentTab.add(eventViewTab)
        contentTab.add(monitorViewTab)

        val content = VerticalLayout()
        content.isSpacing = false
        content.isPadding = false
        content.setSizeFull()

        val eventView = EventView()
        EventGuiController.setGrid(eventView.eventGrid)
        content.add(eventView)

        setSizeFull()
        isSpacing = false
        isPadding = false
        add(contentTab, content)

        contentTab.addSelectedChangeListener {
            content.removeAll()

            if(it.selectedTab == eventViewTab) {
                val newEventView = EventView()
                EventGuiController.setGrid(newEventView.eventGrid)
                content.add(newEventView)
            } else {
                EventGuiController.stopTracking()
                val monitorView = MonitorView()
                MonitorGuiController.setMonitorView(monitorView)
                monitorView.init()
                content.add(monitorView)
            }
        }
    }
}