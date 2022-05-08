package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.orderedlayout.VerticalLayout

class EventView : VerticalLayout() {
    val eventGrid = EventGrid(EventData::class.java)
    val toolbar = Toolbar()

    init {
        addClassName("stream-view")
        setSizeFull()
        add(toolbar, eventGrid)
    }
}
