package com.yuk.kinesisgui.gui.event

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.UIScope

@UIScope
@SpringComponent
class EventView(
    private val eventGrid: EventGrid,
    private val toolbar: Toolbar
) : VerticalLayout() {
    init {
        addClassName("stream-view")
        setSizeFull()
        add(toolbar, eventGrid)
    }
}
