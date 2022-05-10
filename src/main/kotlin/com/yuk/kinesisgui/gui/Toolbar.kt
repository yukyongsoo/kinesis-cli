package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import org.vaadin.olli.FileDownloadWrapper


class Toolbar : HorizontalLayout() {

    init {
        addClassName("toolbar")

        val recordText = TextField().apply {
            placeholder = "add Stream Data"
            isClearButtonVisible = true
            valueChangeMode = ValueChangeMode.LAZY
            width = "900px"
        }

        val button = Button("Add Record")
        button.addClickListener {
            GuiController.addRecord(recordText.value)
        }

        val checkBox = Checkbox("Trim Horizon")
        checkBox.addValueChangeListener {
            GuiController.trimHorizon(checkBox.value)
        }

        val link = FileDownloadWrapper("event.csv") {
            GuiController.exportExcel()
        }
        link.setText("Download CSV")

        add(recordText, button, checkBox, link)
    }
}
