package com.yuk.kinesisgui.gui.event

import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.datetimepicker.DateTimePicker
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.shared.Tooltip
import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.UIScope
import com.yuk.kinesisgui.ExcelUtil
import org.vaadin.olli.FileDownloadWrapper
import org.vaadin.spinkit.Spinner
import org.vaadin.spinkit.SpinnerType

@UIScope
@SpringComponent
class Toolbar(
    private val eventGrid: EventGrid,
) : HorizontalLayout() {
    val dateTimePicker = DateTimePicker("after Time")
    val checkBox = Checkbox("Trim Horizon")
    val tailCheckBox = Checkbox("Tailing")
    val recordAdder = RecordAdder(eventGrid)
    val spinner = Spinner(SpinnerType.FOLDING_CUBE)
    val tooltip = Tooltip.forComponent(spinner).withPosition(Tooltip.TooltipPosition.TOP_START)
    val toolbarRecordProcessor = ToolbarRecordProcessor(this)

    init {
        addClassName("toolbar")
        alignItems = FlexComponent.Alignment.END

        val button = Button("Add Record")
        button.addClickListener {
            recordAdder.open()
        }

        checkBox.addValueChangeListener {
            if (it.isFromClient) {
                dateTimePicker.value = null
                eventGrid.trimHorizon(checkBox.value)
            }
        }

        dateTimePicker.addValueChangeListener {
            if (it.isFromClient) {
                checkBox.value = false
                eventGrid.afterTime(dateTimePicker.value)
            }
        }

        tailCheckBox.addValueChangeListener {
            if (it.isFromClient) {
                eventGrid.tail(tailCheckBox.value)
            }
        }

        val link =
            FileDownloadWrapper("event.csv") {
                val list = eventGrid.currentItems()
                ExcelUtil.createFile(list)
                ExcelUtil.readFile()
            }
        link.setText("Download CSV")

        add(spinner, button, dateTimePicker, checkBox, tailCheckBox, link)
    }

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)
        checkBox.value = false
        dateTimePicker.value = null
        tailCheckBox.value = false
    }

    fun setSpinnerTooltip(iterator: String) {
        ui.ifPresent {
            it.access {
                tooltip.text = "current iterator: $iterator"
                it.push()
            }
        }
    }
}
