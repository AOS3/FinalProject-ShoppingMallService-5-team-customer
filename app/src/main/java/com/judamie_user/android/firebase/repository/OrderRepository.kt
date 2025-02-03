package com.judamie_user.android.firebase.repository

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.judamie_user.android.firebase.vo.OrderVO
import com.judamie_user.android.util.OrderState
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

        // 픽업완료를 눌러 orderData의 state를 변경한다
        suspend fun updateOrderData(orderDocumentID:String,orderState: OrderState){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("OrderData")
            val documentReference = collectionReference.document(orderDocumentID)

            val updateMap = mapOf(
                "orderState" to orderState.num
            )

            documentReference.update(updateMap).await()
        }

        //리뷰를 작성하면 orderData에 리뷰 아이디를 넣어준다
        suspend fun addReviewDocumentID(orderDocumentID: String,reviewDocumentId: String){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("OrderData")
            val documentReference = collectionReference.document(orderDocumentID)

            val updateMap = mapOf(
                "reviewDocumentID" to reviewDocumentId
            )

            documentReference.update(updateMap).await()
        }


        // 유저 아이디를 통해 오더데이터 가져오기
        suspend fun gettingOrdersByUserID(userDocumentId: String): MutableMap<String, OrderVO> {
            val firestore = FirebaseFirestore.getInstance()
            return try {
                val querySnapshot = firestore.collection("OrderData")
                    .whereEqualTo("userDocumentId", userDocumentId)
                    .get()
                    .await()

                querySnapshot.documents.associate { it.id to (it.toObject(OrderVO::class.java) ?: OrderVO()) }
                    .toMutableMap()
            } catch (e: Exception) {
                mutableMapOf()
            }
        }


    }
}