package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.UIScope
import com.yuk.kinesisgui.gui.event.EventView
import com.yuk.kinesisgui.gui.monitor.MonitorView

@UIScope
@SpringComponent
class MainView(
    private val eventView: EventView,
    private val monitorView: MonitorView
) : VerticalLayout() {
    private lateinit var content: VerticalLayout
    private lateinit var eventViewTab: Tab
    private lateinit var monitorViewTab: Tab
    private lateinit var contentTab: Tabs

    init {
        eventViewTab = Tab("Event View")
        monitorViewTab = Tab("Monitor View")

        contentTab = Tabs()
        contentTab.add(eventViewTab)
        contentTab.add(monitorViewTab)

        content = VerticalLayout()
        content.isSpacing = false
        content.isPadding = false
        content.setSizeFull()

        content.add(eventView)

        setSizeFull()
        isSpacing = false
        isPadding = false
        add(contentTab, content)

        contentTab.addSelectedChangeListener {
            clear()
        }
    }

    fun clear() {
        content.removeAll()

        if (contentTab.selectedTab == eventViewTab) {
            content.add(eventView)
        } else {
            content.add(monitorView)
        }
    }
}
