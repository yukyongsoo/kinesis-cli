package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode

class Toolbar : HorizontalLayout() {
    var filterText = TextField()

    init {
        addClassName("toolbar")

        filterText.placeholder = "Filter by EventData..."
        filterText.isClearButtonVisible = true
        filterText.valueChangeMode = ValueChangeMode.LAZY

        val addContactButton = Button("Search")

        add(filterText, addContactButton)
    }
}
