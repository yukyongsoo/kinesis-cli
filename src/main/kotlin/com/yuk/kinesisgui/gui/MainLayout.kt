package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.yuk.kinesisgui.gui.monitor.MonitorGuiController
import com.yuk.kinesisgui.metric.MetricClassifier
import com.yuk.kinesisgui.stream.KinesisService
import com.yuk.kinesisgui.stream.StreamTrackerManager

@Route(value = "")
@PageTitle("Kinesis GUI By Vaadin")
class MainLayout(
    kinesisService: KinesisService,
    metricClassifier: MetricClassifier,
    streamTrackerManager: StreamTrackerManager
) : AppLayout() {
    init {
        EventGuiController.setKinesisService(kinesisService)
        MonitorGuiController.setMonitorService(metricClassifier)
        EventGuiController.setStreamTrackerManager(streamTrackerManager)

        val title = H1("Event Viewer")
        title.style.set("font-size", "var(--lumo-font-size-l)")["margin"] = "0"

        addToDrawer(StreamView())
        addToNavbar(DrawerToggle(), title)

        content = MainView()
    }
}
