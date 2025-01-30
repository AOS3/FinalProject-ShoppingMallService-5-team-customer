package com.judamie_user.android.firebase.service

import com.judamie_user.android.firebase.model.OrderModel
import com.judamie_user.android.firebase.repository.OrderRepository

class OrderService {

    companion object {
        // 주문 정보 추가
        suspend fun addOrderData(orderModel: OrderModel){
            // 데이터를 VO에 담아준다.
            val orderVO = orderModel.toOrderVO()
            // 저장하는 메서드를 호출
            OrderRepository.addOrderData(orderVO, orderModel.orderDocumentId)
        }
    }
}