package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.yuk.kinesisgui.RecordData

class EventView : VerticalLayout() {
    val eventGrid = EventGrid(RecordData::class.java)
    val toolbar = Toolbar()

    init {
        addClassName("stream-view")
        setSizeFull()
        add(toolbar, eventGrid)
    }
}
