package com.judamie_user.android.firebase.vo

import com.judamie_user.android.firebase.model.OrderPackageModel
import com.judamie_user.android.util.OrderPackageState

class OrderPackageVO {
    // 주문 목록 (Order 문서 ID 리스트)
    var orderDataList = mutableListOf<String>()
    // 주문자 문서 ID
    var orderOwnerId = ""
    // 주문 패키지 상태값
    var orderPackageState = 0
    // 주문 패키지 데이터 생성 시간
    var orderPackageDataTimeStamp = 0L
    // 주문자 픽업 상태
    var orderPickupState:Boolean = false

    fun toOrderPackageModel(orderPackageDocumentId:String) : OrderPackageModel {
        val orderPackageModel = OrderPackageModel()

        orderPackageModel.orderPackageDocumentId = orderPackageDocumentId
        orderPackageModel.orderDataList = ArrayList(orderDataList)
        orderPackageModel.orderOwnerId = orderOwnerId
        orderPackageModel.orderPackageDataTimeStamp = orderPackageDataTimeStamp
        orderPackageModel.orderPickupState = orderPickupState

        when(orderPackageState) {
            OrderPackageState.ORDER_PACKAGE_STATE_ENABLE.num -> orderPackageModel.orderPackageState = OrderPackageState.ORDER_PACKAGE_STATE_ENABLE
            OrderPackageState.ORDER_PACKAGE_STATE_DISABLE.num -> orderPackageModel.orderPackageState = OrderPackageState.ORDER_PACKAGE_STATE_ENABLE
        }

        return orderPackageModel
    }
}