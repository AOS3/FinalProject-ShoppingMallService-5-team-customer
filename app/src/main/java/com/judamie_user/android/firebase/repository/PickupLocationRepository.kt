package com.judamie_user.android.firebase.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.judamie_manager.firebase.vo.CouponVO
import com.judamie_manager.firebase.vo.PickupLocationVO
import kotlinx.coroutines.tasks.await

class PickupLocationRepository {

    companion object{
        // 모든 쿠폰 데이터를 가져오는 메서드
        suspend fun gettingPickupLocationList(): MutableList<PickupLocationVO> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("PickupLocationData")
            val result = collectionReference.get().await()
            return result.toObjects(PickupLocationVO::class.java).toMutableList()
        }
    }
}