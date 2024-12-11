package com.yuk.kinesisgui.gui.event

import com.fasterxml.jackson.module.kotlin.readValue
import com.hilerio.ace.AceEditor
import com.hilerio.ace.AceMode
import com.hilerio.ace.AceTheme
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.yuk.kinesisgui.Config
import com.yuk.kinesisgui.gui.openNotification

class RecordAdder(private val eventGrid: EventGrid) : Dialog() {
    val verticalLayout = VerticalLayout()
    val aceEditor: AceEditor =
        AceEditor().apply {
            mode = AceMode.json
            theme = AceTheme.github
            isReadOnly = false
            isWrap = true
            setFontSize(15)
            setHighlightSelectedWord(true)

            setSizeFull()
        }

    val sendButton = Button("Send")
    val closeButton = Button("Close")
    val jsonPrettyButton = Button("Pretty Json")

    init {
        width = "500px"
        height = "500px"
        isCloseOnEsc = true
        isCloseOnOutsideClick = true
        isResizable = true
        isDraggable = true

        add(verticalLayout)
        verticalLayout.setSizeFull()

        verticalLayout.add(aceEditor)
        verticalLayout.expand(aceEditor)

        footer.add(sendButton)
        footer.add(jsonPrettyButton)
        footer.add(closeButton)

        sendButton.addClickListener {
            eventGrid.addRecord(aceEditor.value)
            openNotification("Record added")
        }

        closeButton.addClickListener {
            close()
        }

        jsonPrettyButton.addClickListener {
            val any = Config.objectMapper.readValue<Any>(aceEditor.value)
            val prettyJson =
                Config.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(any)

            aceEditor.value = prettyJson
        }
    }
}
