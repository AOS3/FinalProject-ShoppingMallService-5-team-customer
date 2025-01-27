package com.judamie_user.android.firebase.model

import com.judamie_user.android.firebase.vo.OrderVO
import com.judamie_user.android.util.OrderState

class OrderModel {

    // 주문 문서 ID
    var orderDocumentId = ""
    // 구매자 문서 ID
    var userDocumentId = ""
    // 판매자 문서 ID
    var sellerDocumentID = ""
    // 주문 생성 시간 (= orderTimeStamp)
    var orderTime = 0L
    // 주류 문서 ID
    var productDocumentId = ""
    // 주류 개당 가격
    var productPrice = 0
    // 할인률
    var productDiscountRate = 0
    // 상품 개수
    var orderCount = 0
    // 총 가격
    var orderPriceAmount = 0
    // 픽업지 문서 ID
    var pickupLocDocumentId = ""
    // 데이터가 들어온 시간
    var orderTimeStamp = 0L
    // 주문 상태
    var orderState = OrderState.ORDER_STATE_PAYMENT_COMPLETE

    fun toOrderVO() : OrderVO {
        val orderVO = OrderVO()

        orderVO.userDocumentId = userDocumentId
        orderVO.sellerDocumentId = sellerDocumentID
        orderVO.orderTime = orderTime
        orderVO.productDocumentId = productDocumentId
        orderVO.productPrice = productPrice
        orderVO.productDiscountRate = productDiscountRate
        orderVO.orderCount = orderCount
        orderVO.orderPriceAmount = orderPriceAmount
        orderVO.pickupLocDocumentId = pickupLocDocumentId
        orderVO.orderTimeStamp = orderTimeStamp
        orderVO.orderState = orderState.num

        return orderVO
    }
}