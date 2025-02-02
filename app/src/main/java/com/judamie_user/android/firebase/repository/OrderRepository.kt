package com.judamie_user.android.firebase.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.judamie_user.android.firebase.vo.OrderVO
import kotlinx.coroutines.tasks.await

class OrderRepository {
    companion object {
        // 주문 정보를 추가하는 메서드
        suspend fun addOrderData(orderVO: OrderVO):String{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("OrderData")
            val documentReference = collectionReference.add(orderVO).await()
            return documentReference.id // 새로 생성된 문서의 ID 반환
        }

        // 주문정보를 가져오는 메서드
        suspend fun gettingOrderData(orderDocumentID:String):OrderVO{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("OrderData")
            val documentReference = collectionReference
                .document(orderDocumentID)
                .get()
                .await()

            val orderVO = documentReference.toObject(OrderVO::class.java)!!
            return orderVO
        }
    }
}