package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.yuk.kinesisgui.KinesisService
import com.yuk.kinesisgui.StreamTrackerManager

@Route(value = "")
@PageTitle("Kinesis GUI By Vaadin")
class MainView(
    kinesisService: KinesisService,
    streamTrackerManager: StreamTrackerManager
) : AppLayout() {
    init {
        GuiController.setKinesisService(kinesisService)
        GuiController.setStreamTrackerManager(streamTrackerManager)

        val title = H1("Event Viewer")
        title.style.set("font-size", "var(--lumo-font-size-l)")["margin"] = "0"

        addToDrawer(StreamView())
        addToNavbar(DrawerToggle(), title)

        val eventView = EventView()
        content = eventView
        GuiController.setGrid(eventView.eventGrid)
    }
}
