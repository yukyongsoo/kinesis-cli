package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.vaadin.olli.ClipboardHelper

class ModalDialog(
    private val title: String,
    private var content: String
) : Dialog() {
    init {
        headerTitle = title

        val dialogLayout = VerticalLayout()
        dialogLayout.add(content)

        val cancelButton = Button("Cancel") { e -> this.close() }
        val copyButton = Button("Copy")
        val helper = ClipboardHelper(content, copyButton)

        footer.add(helper)
        footer.add(cancelButton)

        add(dialogLayout)
    }
}
