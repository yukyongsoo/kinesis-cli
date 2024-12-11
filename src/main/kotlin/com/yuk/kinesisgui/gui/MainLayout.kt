package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.yuk.kinesisgui.gui.stream.StreamListView

@Route(value = "")
@PageTitle("Kinesis GUI By Vaadin")
class MainLayout(
    private val mainView: MainView,
    private val streamListView: StreamListView,
) : AppLayout() {
    init {
        val title = H1("Event Viewer")
        title.style.set("font-size", "var(--lumo-font-size-l)")["margin"] = "0"

        addToDrawer(streamListView)
        addToNavbar(DrawerToggle(), title)

        content = mainView
    }
}
