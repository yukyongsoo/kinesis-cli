package com.yuk.kinesisgui.gui

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.server.ErrorEvent
import com.vaadin.flow.server.ErrorHandler
import com.vaadin.flow.server.ServiceInitEvent
import com.vaadin.flow.server.VaadinServiceInitListener

class ServiceListener : VaadinServiceInitListener {
    override fun serviceInit(event: ServiceInitEvent) {
        event.source.addSessionInitListener {
            it.session.errorHandler = CustomErrorHandler()
        }
    }
}

class CustomErrorHandler : ErrorHandler {
    override fun error(errorEvent: ErrorEvent) {
        if (UI.getCurrent() != null) {
            UI.getCurrent().access {
                val notification = Notification("An internal error has occurred. ${errorEvent.throwable.message}")
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR)
                notification.open()
            }
        }
    }
}
