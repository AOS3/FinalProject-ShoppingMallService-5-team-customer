package com.judamie_user.android.firebase.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.judamie_manager.firebase.vo.CouponVO
import com.judamie_user.android.firebase.vo.OrderPackageVO
import com.judamie_user.android.firebase.vo.OrderVO
import kotlinx.coroutines.tasks.await

class OrderPackageRepository {
    companion object {
        // 주문 패키지를 추가하는 메서드
        suspend fun addOrderPackageData(orderPackageVO: OrderPackageVO, orderPackageDocumentId: String){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("OrderPackageData")

            collectionReference.add(orderPackageVO)
        }

        // 주문 패키지를 가져오는메서드
        suspend fun gettingOrderPackageData(userDocumentID: String): Map<String, OrderPackageVO> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("OrderPackageData")

            return try {
                val result = collectionReference
                    .whereEqualTo("orderOwnerId", userDocumentID)
                    .get()
                    .await()

                result.documents.associate { document ->
                    document.id to document.toObject(OrderPackageVO::class.java)!!
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyMap()
            }
        }


    }
}