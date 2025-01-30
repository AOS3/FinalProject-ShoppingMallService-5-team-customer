package com.judamie_user.android.firebase.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.judamie_user.android.firebase.vo.OrderVO

class OrderRepository {
    companion object {
        // 주문 정보를 추가하는 메서드
        suspend fun addOrderData(orderVO: OrderVO, orderDocumentId: String) {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("OrderData")
            collectionReference.add(orderVO)
        }
    }
}