package com.yuk.kinesisgui.gui.stream

import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.UIScope

@UIScope
@SpringComponent
class StreamListView(
    private val streamTabs: StreamTabs,
) : VerticalLayout() {
    init {
        val searchField = TextField()
        searchField.placeholder = "Press Enter"
        searchField.prefixComponent = VaadinIcon.SEARCH.create()
        searchField.isClearButtonVisible = true
        searchField.addValueChangeListener {
            streamTabs.filterStream(searchField.value)
        }

        add(searchField, streamTabs)
    }
}
