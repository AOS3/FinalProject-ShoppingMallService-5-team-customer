package com.judamie_user.android.firebase.vo

import com.judamie_user.android.firebase.model.OrderModel
import com.judamie_user.android.firebase.model.ProductModel
import com.judamie_user.android.util.OrderState
import com.judamie_user.android.util.ProductState

class OrderVO {

    // 구매자 문서 ID
    var userDocumentId = ""
    // 판매자 문서 ID
    var sellerDocumentId = ""
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
    var orderState = 0

    fun toOrderModel(orderDocumentId:String) : OrderModel {
        val orderModel = OrderModel()

        orderModel.orderDocumentId = orderDocumentId
        orderModel.userDocumentId = userDocumentId
        orderModel.sellerDocumentID = sellerDocumentId
        orderModel.orderTime = orderTime
        orderModel.productDocumentId = productDocumentId
        orderModel.productPrice = productPrice
        orderModel.productDiscountRate = productDiscountRate
        orderModel.orderCount = orderCount
        orderModel.orderPriceAmount = orderPriceAmount
        orderModel.pickupLocDocumentId = pickupLocDocumentId
        orderModel.orderTimeStamp = orderTimeStamp

        when (orderState){
            OrderState.ORDER_STATE_PAYMENT_COMPLETE.num -> orderModel.orderState = OrderState.ORDER_STATE_PAYMENT_COMPLETE
            OrderState.ORDER_STATE_BEFORE_DELIVERY.num -> orderModel.orderState = OrderState.ORDER_STATE_BEFORE_DELIVERY
            OrderState.ORDER_STATE_DELIVERY.num -> orderModel.orderState = OrderState.ORDER_STATE_DELIVERY
            OrderState.ORDER_STATE_BEFORE_PICKUP.num -> orderModel.orderState = OrderState.ORDER_STATE_BEFORE_PICKUP
            OrderState.ORDER_STATE_PICKUP_COMPLETED.num -> orderModel.orderState = OrderState.ORDER_STATE_PICKUP_COMPLETED
            OrderState.ORDER_STATE_BEFORE_TRANSFER.num -> orderModel.orderState = OrderState.ORDER_STATE_BEFORE_TRANSFER
            OrderState.ORDER_STATE_TRANSFER_COMPLETED.num -> orderModel.orderState = OrderState.ORDER_STATE_TRANSFER_COMPLETED
        }

        return orderModel
    }
}