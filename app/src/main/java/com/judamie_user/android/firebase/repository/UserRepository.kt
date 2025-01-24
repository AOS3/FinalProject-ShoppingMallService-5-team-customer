package com.judamie_user.android.firebase.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    companion object{

        suspend fun gettingUserNameByID(userDocumentID: String): String {
            val db = FirebaseFirestore.getInstance()

            return try {
                // Firestore에서 데이터를 가져옴
                val document = db.collection("UserData")
                    .document(userDocumentID)
                    .get()
                    .await() // await()을 사용하여 비동기 Task를 처리

                if (document.exists()) {
                    document.getString("userName") ?: "Unknown User"
                } else {
                    "Document not found"
                }
            } catch (e: Exception) {
                // 예외 처리
                e.printStackTrace()
                "Error fetching data"
            }
        }


    }
}