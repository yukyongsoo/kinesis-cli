package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.textfield.TextFieldVariant
import com.vaadin.flow.data.value.ValueChangeMode
import java.util.function.Consumer

class EventGrid(clazz: Class<EventData>) : Grid<EventData>(clazz, false) {
    private val items = mutableListOf<EventData>()
    private val eventGridSearchFilter: EventGridSearchFilter

    init {
        addClassNames("contact-grid")
        setSizeFull()

        val dataView = setItems(items)
        eventGridSearchFilter = EventGridSearchFilter(dataView)
        setHeader()
    }

    private fun setHeader() {
        val timeColumn: Column<EventData> = addColumn(EventData::eventTime)
        val typeColumn: Column<EventData> = addColumn(EventData::eventType)
        val sourceColumn: Column<EventData> = addColumn(EventData::source)
        val dataColumn: Column<EventData> = addColumn(EventData::data)

        headerRows.clear()
        val headerRow = appendHeaderRow()

        headerRow.getCell(timeColumn).setComponent(
            createFilterHeader("Time") {
                eventGridSearchFilter.eventTime
            }
        )
        headerRow.getCell(typeColumn)
            .setComponent(
                createFilterHeader("EventType") {
                    eventGridSearchFilter.eventType
                }
            )
        headerRow.getCell(sourceColumn).setComponent(
            createFilterHeader("Source") {
                eventGridSearchFilter.source
            }
        )
        headerRow.getCell(dataColumn).setComponent(
            createFilterHeader("Data") {
                eventGridSearchFilter.data
            }
        )
    }

    private fun createFilterHeader(
        labelText: String,
        filterChangeConsumer: Consumer<String>
    ): Component {
        val label = Label(labelText).apply {
            style["padding-top"] = "var(--lumo-space-m)"
            style["font-size"] = "var(--lumo-font-size-xs)"
        }

        val textField = TextField().apply {
            valueChangeMode = ValueChangeMode.EAGER
            isClearButtonVisible = true
            addThemeVariants(TextFieldVariant.LUMO_SMALL)
            setWidthFull()
            style["max-width"] =  "100%"
        }
        textField.addValueChangeListener { e ->
            filterChangeConsumer.accept(e.value)
        }

        val layout = VerticalLayout(label, textField).apply {
            themeList.clear()
            themeList.add("spacing-xs")
        }

        return layout
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
