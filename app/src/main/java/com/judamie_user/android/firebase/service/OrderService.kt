package com.judamie_user.android.firebase.service

import com.judamie_user.android.firebase.model.OrderModel
import com.judamie_user.android.firebase.repository.OrderRepository
import com.judamie_user.android.firebase.vo.OrderVO

class OrderService {

    companion object {
        // 주문 정보 추가
        suspend fun addOrderData(orderModel: OrderModel):String{
            // 데이터를 vo에 담아준다.
            val orderVO = orderModel.toOrderVO()
            // 저장하는 메서드를 호출
            val orderDocumentID = OrderRepository.addOrderData(orderVO)
            return orderDocumentID
        }

        // 주문정보를 가져오는 메서드
        suspend fun gettingOrderData(orderDocumentID:String): OrderModel {
            val orderModel = OrderRepository.gettingOrderData(orderDocumentID).toOrderModel(orderDocumentID)
            return orderModel
        }
    }
}