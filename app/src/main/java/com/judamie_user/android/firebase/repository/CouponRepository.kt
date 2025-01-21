package com.judamie_user.android.firebase.repository


import com.judamie_manager.firebase.vo.CouponVO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.judamie_manager.firebase.model.CouponModel
import kotlinx.coroutines.tasks.await

class CouponRepository {
    companion object{

        // 모든 쿠폰 데이터를 가져오는 메서드
        suspend fun gettingCouponList(): MutableList<CouponVO> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("CouponData")
            val result = collectionReference.get().await()
            return result.toObjects(CouponVO::class.java).toMutableList()
        }



    }
}