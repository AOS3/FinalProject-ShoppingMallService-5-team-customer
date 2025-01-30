package com.judamie_user.android.firebase.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.judamie_user.android.firebase.vo.OrderPackageVO
import com.judamie_user.android.firebase.vo.OrderVO

class OrderPackageRepository {
    companion object {
        // 주문 패키지를 추가하는 메서드
        suspend fun addOrderPackageData(orderPackageVO: OrderPackageVO, orderPackageDocumentId: String) {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("OrderPackageData")
            collectionReference.add(orderPackageVO)
        }
    }
}