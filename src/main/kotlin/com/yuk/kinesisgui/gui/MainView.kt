package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.yuk.kinesisgui.KinesisService

@Route(value = "")
@PageTitle("Kinesis GUI By Vaadin")
class MainView(
    private val kinesisService: KinesisService,
) : AppLayout() {
    val streamView = StreamView(kinesisService)
    val eventView = EventView()

    init {
        val title = H1("Event Viewer")
        title.style.set("font-size", "var(--lumo-font-size-l)")["margin"] = "0"

        addToDrawer(streamView)
        addToNavbar(DrawerToggle(), title)
        content = eventView
    }
}
