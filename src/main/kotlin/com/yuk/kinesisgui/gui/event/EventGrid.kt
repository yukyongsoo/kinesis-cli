package com.yuk.kinesisgui.gui.event

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.DetachEvent
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.textfield.TextFieldVariant
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.UIScope
import com.yuk.kinesisgui.gui.ModalDialog
import com.yuk.kinesisgui.gui.SessionContext
import com.yuk.kinesisgui.stream.RecordData
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentSkipListSet
import java.util.function.Consumer

@UIScope
@SpringComponent
class EventGrid(
    clazz: Class<RecordData> = RecordData::class.java,
    private val sessionContext: SessionContext
) : Grid<RecordData>(clazz, false) {
    private val items = ConcurrentSkipListSet<RecordData>()
    private val maxSize = 30000
    private val eventGridSearchFilter = EventGridSearchFilter()
    val gridRecordProcessor = GridRecordProcessor(this)

    init {
        addClassNames("contact-grid")
        setSizeFull()

        val recordTimeColumn = addColumn(RecordData::recordTime).setAutoWidth(true)
        val timeColumn = addColumn(RecordData::eventTime).setAutoWidth(true)
        val shardIdColumn = addColumn(RecordData::shardId).setAutoWidth(true)
        val seqColumn = addColumn(RecordData::seq).setAutoWidth(true)
        val typeColumn = addColumn(RecordData::eventType).setAutoWidth(true)
        val sourceColumn = addColumn(RecordData::source).setAutoWidth(true)
        val dataColumn = addColumn(RecordData::data).setAutoWidth(true)

        setHeader(recordTimeColumn, timeColumn, shardIdColumn, seqColumn, typeColumn, sourceColumn, dataColumn)

        addItemClickListener {
            ModalDialog(it.item.seq, it.item.raw).open()
        }

        addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT)
    }

    override fun onDetach(detachEvent: DetachEvent) {
        super.onDetach(detachEvent)
        clean()
    }

    fun addRecord(value: String) {
        sessionContext.addRecord(value)
    }

    fun trimHorizon(value: Boolean) {
        clean()
        sessionContext.trimHorizon(value)
    }

    fun afterTime(value: LocalDateTime) {
        clean()
        sessionContext.afterTime(value)
    }

    fun addItems(items: Collection<RecordData>) = refreshUI {
        this.items.addAll(items)
        dropMaxItems()
    }

    fun clean() = refreshUI {
        this.items.clear()
    }

    fun currentItems() = items.filter(eventGridSearchFilter::filter)

    private fun setHeader(
        recordColumn: Column<RecordData>,
        timeColumn: Column<RecordData>,
        shardIdColumn: Column<RecordData>,
        seqColumn: Column<RecordData>,
        typeColumn: Column<RecordData>,
        sourceColumn: Column<RecordData>,
        dataColumn: Column<RecordData>
    ) {
        headerRows.clear()
        val headerRow = appendHeaderRow()

        headerRow.getCell(recordColumn).setComponent(
            createFilterHeader("RecordTime", filterChangeConsumer = eventGridSearchFilter::recordTime::set)
        )
        headerRow.getCell(timeColumn).setComponent(
            createFilterHeader("Time", filterChangeConsumer = eventGridSearchFilter::eventTime::set)
        )
        headerRow.getCell(shardIdColumn).setComponent(
            createFilterHeader("shardId", filterChangeConsumer = eventGridSearchFilter::shardId::set)
        )

        headerRow.getCell(seqColumn).setComponent(
            createFilterHeader("Seq", filterChangeConsumer = eventGridSearchFilter::seq::set)
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
            refreshUI {}
        }

        val layout = VerticalLayout(label, textField).apply {
            themeList.clear()
            themeList.add("spacing-xs")
        }

        return layout
    }

    private fun dropMaxItems() = refreshUI {
        while (items.size > maxSize) {
            items.remove(items.first())
        }
    }

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
