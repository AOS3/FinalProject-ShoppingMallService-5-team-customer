package com.judamie_user.android.firebase.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.judamie_manager.firebase.vo.CouponVO
import com.judamie_manager.firebase.vo.PickupLocationVO
import kotlinx.coroutines.tasks.await

class PickupLocationRepository {

    companion object {
        //픽업지를 가져오는 메서드
        suspend fun gettingPickupLocationList(): MutableList<PickupLocationVO> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("PickupLocationData")
            val result = collectionReference.get().await()
            return result.toObjects(PickupLocationVO::class.java).toMutableList()
        }

        // 유저의 픽업지 정보를 가져오는 메서드
        suspend fun getUserPickupLocation(userDocumentID: String): String? {
            val firestore = FirebaseFirestore.getInstance()
            val documentSnapshot = firestore.collection("UserData")
                .document(userDocumentID)
                .get()
                .await()
            return documentSnapshot.getString("userPickupLoc")
        }

    }
}