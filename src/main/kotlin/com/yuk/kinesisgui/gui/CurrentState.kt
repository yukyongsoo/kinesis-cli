package com.yuk.kinesisgui.gui

import com.yuk.kinesisgui.gui.monitor.MonitorGuiController
import kotlin.properties.Delegates

object CurrentState {
    var streamName: String by Delegates.observable("") { _, _, value ->
        EventGuiController.selectedStream()
        MonitorGuiController.setMetric()
    }
}
