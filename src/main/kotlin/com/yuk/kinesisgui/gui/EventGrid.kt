package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.textfield.TextFieldVariant
import com.vaadin.flow.data.value.ValueChangeMode
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.function.Consumer
import kotlin.math.min

class EventGrid(clazz: Class<EventData>) : Grid<EventData>(clazz, false) {
    private val items = ConcurrentLinkedQueue<EventData>()
    private val eventGridSearchFilter = EventGridSearchFilter()

    init {
        addClassNames("contact-grid")
        setSizeFull()

        val timeColumn = addColumn(EventData::eventTime).setAutoWidth(true)
        val typeColumn = addColumn(EventData::eventType).setAutoWidth(true)
        val sourceColumn = addColumn(EventData::source).setAutoWidth(true)
        val dataColumn = addColumn(EventData::data).setAutoWidth(true)

        addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT)

        setHeader(timeColumn, typeColumn, sourceColumn, dataColumn)
    }

    private fun setHeader(
        timeColumn: Column<EventData>,
        typeColumn: Column<EventData>,
        sourceColumn: Column<EventData>,
        dataColumn: Column<EventData>
    ) {
        headerRows.clear()
        val headerRow = appendHeaderRow()

        headerRow.getCell(timeColumn).setComponent(
            createFilterHeader("Time", filterChangeConsumer =  eventGridSearchFilter::eventTime::set)
        )
        headerRow.getCell(typeColumn)
            .setComponent(
                createFilterHeader("EventType", filterChangeConsumer = eventGridSearchFilter::eventType::set)
            )
        headerRow.getCell(sourceColumn).setComponent(
            createFilterHeader("Source", filterChangeConsumer = eventGridSearchFilter::source::set)
        )
        headerRow.getCell(dataColumn).setComponent(
            createFilterHeader("Data", "500px", eventGridSearchFilter::data::set)
        )
    }

    private fun createFilterHeader(
        labelText: String,
        minWidth: String = "",
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
            if (minWidth.isNotBlank())
                this.minWidth = minWidth
            style["max-width"] = "100%"
        }
        textField.addValueChangeListener { e ->
            filterChangeConsumer.accept(e.value)
            refreshUI{}
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

    fun currentItems() = items.filter(eventGridSearchFilter::filter)

    private fun refreshUI(block: () -> Unit) {
        block()
        val filteredItems = items.filter(eventGridSearchFilter::filter)

        ui.ifPresent {
            it.access {
                setItems(filteredItems)
                it.push()
            }
        }
    }
}
