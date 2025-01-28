package com.judamie_user.android.firebase.repository

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.judamie_manager.firebase.vo.CouponVO
import com.judamie_manager.firebase.vo.PickupLocationVO
import com.judamie_user.android.firebase.model.PickupLocationModel
import kotlinx.coroutines.tasks.await

class PickupLocationRepository {

    companion object {
        suspend fun gettingPickupLocationList(): MutableList<PickupLocationModel> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("PickupLocationData")
            val result = collectionReference.get().await()
            val pickupLocationList = mutableListOf<PickupLocationModel>()

            for (document in result.documents) {
                val pickupLocation = document.toObject(PickupLocationModel::class.java)
                if (pickupLocation != null) {
                    pickupLocation.pickupLocDocumentID = document.id // 문서 ID 추가
                    pickupLocationList.add(pickupLocation)
                }
            }

            return pickupLocationList
        }



        // 유저의 픽업지 정보를 가져오는 메서드
        suspend fun gettingUserPickupLocation(userDocumentID: String): String? {
            val firestore = FirebaseFirestore.getInstance()
            val documentSnapshot = firestore.collection("UserData")
                .document(userDocumentID)
                .get()
                .await()
            return documentSnapshot.getString("userPickupLoc")
        }

        // 유저의 픽업지 정보를 설정하는 함수
        suspend fun settingUserPickupLocation(userDocumentID: String, pickLocationDocumentID: String) {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val documentReference = collectionReference.document(userDocumentID)

            // userPickupLoc 필드에 새로운 pickLocationDocumentID 값으로 설정
            documentReference.update("userPickupLoc", pickLocationDocumentID).await()
        }

        suspend fun gettingPickupLocationById(pickLocationDocumentID: String): PickupLocationVO {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("PickupLocationData")
            val documentReference = collectionReference.document(pickLocationDocumentID)

            // Firestore에서 문서를 가져옴
            val documentSnapshot = documentReference.get().await()

            // 문서를 Model 객체로 변환
            val pickupLocationVO = documentSnapshot.toObject(PickupLocationVO::class.java)

            return pickupLocationVO!!
        }






    }
}