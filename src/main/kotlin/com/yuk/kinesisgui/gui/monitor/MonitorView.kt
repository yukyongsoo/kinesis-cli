package com.yuk.kinesisgui.gui.monitor

import com.vaadin.componentfactory.gridlayout.GridLayout
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.DetachEvent
import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.UIScope
import com.yuk.kinesisgui.gui.Chart
import com.yuk.kinesisgui.gui.SessionContext
import com.yuk.kinesisgui.metric.MetricDataSet

@UIScope
@SpringComponent
class MonitorView(
    private val sessionContext: SessionContext,
) : GridLayout(3, 10) {
    val putRecordsTotal = Chart("총 생성 요청 레코드 수")
    val putRecordsByte = Chart("생성 레코드 Byte 사이즈")
    val putRecordLatency = Chart("레코드 생성 응답속도(Ms)")
    val putRecordThrottled = Chart("지연이 발생한 레코드 수")
    val putRecordsSuccess = Chart("성공한 생성 레코드 수")
    val putRecordsFailed = Chart("실패한 생성 레코드 수")
    val incomingRecords = Chart("인입된 레코드 수")
    val subscribeToShardEventMillisBehindLatest = Chart("이벤트 구독 지연(Ms)")
    val subscribeToShardEventSuccess = Chart("이벤트 구독 성공")
    val subscribeToShardRecord = Chart("이벤트 구독 레코드 수")
    val subscribeToShardRateExceeded = Chart("이벤트 구독 요청 초과")
    val writeProvisionedThroughputExceeded = Chart("생성 제한량 초과")

    init {
        setSizeFull()
        setMargin(true)

        addComponent(putRecordsTotal)
        addComponent(putRecordsByte)
        addComponent(putRecordLatency)
        addComponent(putRecordThrottled)
        addComponent(putRecordsSuccess)
        addComponent(putRecordsFailed)
        addComponent(incomingRecords)
        addComponent(subscribeToShardEventMillisBehindLatest)
        addComponent(subscribeToShardEventSuccess)
        addComponent(subscribeToShardRecord)
        addComponent(subscribeToShardRateExceeded)
        addComponent(writeProvisionedThroughputExceeded)
    }

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)

        val metrics = sessionContext.getMetrics()
        setChart(metrics)
    }

    override fun onDetach(detachEvent: DetachEvent?) {
        super.onDetach(detachEvent)
        clear()
    }

    private fun setChart(dataSet: MetricDataSet) {
        putRecordsTotal.addAllData(dataSet.putRecordsTotal)
        putRecordsByte.addAllData(dataSet.putRecordsByte)
        putRecordLatency.addAllData(dataSet.putRecordLatency)
        putRecordThrottled.addAllData(dataSet.putRecordThrottled)
        putRecordsSuccess.addAllData(dataSet.putRecordsSuccess)
        putRecordsFailed.addAllData(dataSet.putRecordsFailed)
        incomingRecords.addAllData(dataSet.incomingRecords)
        subscribeToShardEventMillisBehindLatest.addAllData(dataSet.subscribeToShardEventMillisBehindLatest)
        subscribeToShardEventSuccess.addAllData(dataSet.subscribeToShardEventSuccess)
        subscribeToShardRecord.addAllData(dataSet.subscribeToShardRecord)
        subscribeToShardRateExceeded.addAllData(dataSet.subscribeToShardRateExceeded)
        writeProvisionedThroughputExceeded.addAllData(dataSet.writeProvisionedThroughputExceeded)

        ui.ifPresent {
            it.access {
                it.push()
            }
        }
    }

    private fun clear() {
        putRecordsTotal.clear()
        putRecordsByte.clear()
        putRecordLatency.clear()
        putRecordThrottled.clear()
        putRecordsSuccess.clear()
        putRecordsFailed.clear()
        incomingRecords.clear()
        subscribeToShardEventMillisBehindLatest.clear()
        subscribeToShardEventSuccess.clear()
        subscribeToShardRecord.clear()
        subscribeToShardRateExceeded.clear()
        writeProvisionedThroughputExceeded.clear()
    }
}
