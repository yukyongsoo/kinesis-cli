package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs

class MainView : VerticalLayout() {
    private val eventView: EventView
    private val monitorView: MonitorView

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

        eventView = EventView()
        monitorView = MonitorView()
        content.add(eventView)

        setSizeFull()
        isSpacing = false
        isPadding = false
        add(contentTab, content)

        contentTab.addSelectedChangeListener {
            content.removeAll()

            if (it.selectedTab == eventViewTab) {
                content.add(eventView)
            } else {
                content.add(monitorView)
            }
        }
    }
}
