package com.judamie_user.android.firebase.model

import com.judamie_user.android.firebase.vo.OrderPackageVO
import com.judamie_user.android.util.OrderPackageState

class OrderPackageModel {
    // 주문 패키지 문서 ID
    var orderPackageDocumentId = ""
    // 주문 목록 (Order 문서 ID 리스트)
    var orderDataList = mutableListOf<String>()
    // 주문자 문서 ID
    var orderOwnerId = ""
    // 주문 패키지 상태값
    var orderPackageState = OrderPackageState.ORDER_PACKAGE_STATE_ENABLE
    // 주문 패키지 데이터 생성 시간
    var orderPackageDataTimeStamp = 0L
    // 주문자 픽업 상태
    var orderPickupState:Boolean = false

    fun toOrderPackageVO() : OrderPackageVO {
        val orderPackageVO = OrderPackageVO()

        orderPackageVO.orderDataList = ArrayList(orderDataList)
        orderPackageVO.orderOwnerId = orderOwnerId
        orderPackageVO.orderPackageDataTimeStamp = orderPackageDataTimeStamp
        orderPackageVO.orderPickupState = orderPickupState

        orderPackageVO.orderPackageState = orderPackageState.num

        return orderPackageVO
    }
}