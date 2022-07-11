package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.datetimepicker.DateTimePicker
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import org.vaadin.olli.FileDownloadWrapper

class Toolbar : HorizontalLayout() {
    val dateTimePicker = DateTimePicker("after Time")
    init {
        addClassName("toolbar")
        alignItems = FlexComponent.Alignment.END

        val recordText = TextField().apply {
            placeholder = "add Stream Data"
            isClearButtonVisible = true
            valueChangeMode = ValueChangeMode.LAZY
            width = "800px"
        }

        val button = Button("Add Record")
        button.addClickListener {
            dateTimePicker.value = null
            EventGuiController.addRecord(recordText.value)
        }

        val checkBox = Checkbox("Trim Horizon")
        checkBox.addValueChangeListener {
            EventGuiController.trimHorizon(checkBox.value)
        }

        dateTimePicker.addValueChangeListener {
            checkBox.value = false
            EventGuiController.afterTime(dateTimePicker.value)
        }

        val link = FileDownloadWrapper("event.csv") {
            EventGuiController.exportExcel()
        }
        link.setText("Download CSV")

        add(recordText, button, dateTimePicker, checkBox, link)
    }
}
