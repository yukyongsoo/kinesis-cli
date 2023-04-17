package com.yuk.kinesisgui.gui.stream

import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.UIScope
import com.yuk.kinesisgui.gui.MainView
import com.yuk.kinesisgui.gui.SessionContext

@UIScope
@SpringComponent
class StreamTabs(
    private val sessionContext: SessionContext,
    private val mainView: MainView
) : Tabs() {
    init {
        orientation = Tabs.Orientation.VERTICAL

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

    fun filterStream(value: String) {
        val hideTarget = this.children.filter {
            it is Tab && it.label.contains(value).not()
        }

        val showTarget = this.children.filter {
            it is Tab && it.label.contains(value)
        }

        hideTarget.forEach { it.isVisible = false }
        showTarget.forEach { it.isVisible = true }
    }
}
