package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.UIScope

@UIScope
@SpringComponent
class StreamListView(
    private val sessionContext: SessionContext,
    private val mainView: MainView
) : Tabs() {
    init {
        orientation = Orientation.VERTICAL

        val list = sessionContext.getStreamList()
        list.sorted().forEach {
            add(Tab(it))
        }

        sessionContext.changeCurrentStreamName(list.first())

        addSelectedChangeListener { event ->
            mainView.clear()
            sessionContext.changeCurrentStreamName(event.selectedTab.label)
        }
    }
}
