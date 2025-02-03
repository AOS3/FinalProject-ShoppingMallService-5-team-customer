package com.judamie_user.android.firebase.service

import com.judamie_user.android.firebase.model.OrderModel
import com.judamie_user.android.firebase.repository.OrderRepository
import com.judamie_user.android.firebase.vo.OrderVO
import com.judamie_user.android.util.OrderState

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

        // 픽업완료를 눌러 orderData의 state를 변경한다
        suspend fun updateOrderData(orderDocumentID:String,orderState: OrderState){
            OrderRepository.updateOrderData(orderDocumentID,orderState)
        }

        //리뷰를 작성하면 orderData에 리뷰 아이디를 넣어준다
        suspend fun addReviewDocumentID(orderDocumentID: String,reviewDocumentId: String){
            OrderRepository.addReviewDocumentID(orderDocumentID,reviewDocumentId)
        }
    }
}