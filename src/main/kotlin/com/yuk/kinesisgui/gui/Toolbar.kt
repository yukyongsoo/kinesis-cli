package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode

class Toolbar : HorizontalLayout() {
    var recordText = TextField()

    init {
        addClassName("toolbar")

        recordText.placeholder = "add Stream Data"
        recordText.isClearButtonVisible = true
        recordText.valueChangeMode = ValueChangeMode.LAZY
        recordText.width = "900px"

        val button = Button("Add Record")
        button.addClickListener {
            GuiController.addRecord(recordText.value)
        }

        add(recordText, button)
    }
}
