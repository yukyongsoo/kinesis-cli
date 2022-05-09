package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.grid.Grid

class EventGrid(clazz: Class<EventData>) : Grid<EventData>(clazz) {
    private val items = mutableListOf<EventData>()

    init {
        addClassNames("contact-grid")
        setSizeFull()
        setColumns("eventTime", "eventType", "source", "data")
        columns.forEach { col -> col.setAutoWidth(true) }
    }

    fun addItem(item: EventData) = refreshUI {
        items.add(item)
    }


    fun addItems(items: Collection<EventData>) = refreshUI {
        this.items.addAll(items)
    }

    fun clean() = refreshUI {
        this.items.clear()
    }


    private fun refreshUI(block: () -> Unit) {
        ui.ifPresent {
            it.access {
                block()
                setItems(items)
                it.push()
            }
        }
    }
}
