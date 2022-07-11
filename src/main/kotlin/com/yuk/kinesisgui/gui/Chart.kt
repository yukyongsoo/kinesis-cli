package com.yuk.kinesisgui.gui

import com.storedobject.chart.Data
import com.storedobject.chart.DataType
import com.storedobject.chart.Legend
import com.storedobject.chart.LineChart
import com.storedobject.chart.Position
import com.storedobject.chart.RectangularCoordinate
import com.storedobject.chart.SOChart
import com.storedobject.chart.TimeData
import com.storedobject.chart.Title
import com.storedobject.chart.XAxis
import com.storedobject.chart.YAxis
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import java.time.LocalDateTime


class Chart(
    private val name: String
): VerticalLayout() {
    val chart: SOChart
    val xValues = TimeData()
    val yValues = Data()

    init {
        setSizeFull()
        isPadding = false

        chart = SOChart()
        chart.setMinHeight(200f, com.vaadin.flow.component.Unit.PIXELS)
        chart.setMinWidth(150f, com.vaadin.flow.component.Unit.PIXELS)

        val lineChart = LineChart(xValues, yValues)
        lineChart.name = name

        val position = Position().apply {
            alignBottom()
        }
        val legend = Legend().apply {
            setPosition(position)
        }

        val xAxis = XAxis(DataType.DATE)
        val yAxis = YAxis(DataType.NUMBER)
        val rc = RectangularCoordinate(xAxis, yAxis)
        lineChart.plotOn(rc)

        chart.add(lineChart, Title(name), legend)

        add(chart)
    }

    fun addData(dateTime: LocalDateTime, data: Double) {
        xValues.add(dateTime)
        yValues.add(data)

        ui.ifPresent {
            it.access {
                chart.update()
                it.push()
            }
        }
    }

    fun addAllData(data: List<Pair<LocalDateTime,Double>>) {
        data.forEach { (dateTime, data) ->
            xValues.add(dateTime)
            yValues.add(data)
        }

        ui.ifPresent {
            it.access {
                chart.update()
                it.push()
            }
        }
    }

    fun clear() {
        xValues.clear()
        yValues.clear()
    }
}