package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.grid.Grid

class EventGrid(clazz: Class<EventData>) : Grid<EventData>(clazz) {
    init {
        addClassNames("contact-grid")
        setSizeFull()
        setColumns("shardName", "eventTime", "eventData")
        columns.forEach { col -> col.setAutoWidth(true) }
    }
}
