package com.judamie_user.android.firebase.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.judamie_user.android.firebase.vo.UserVO
import kotlinx.coroutines.tasks.await

class UserRepository {
    companion object{

        // 사용자 아이디를 통해 사용자 데이터를 가져오는 메서드
        suspend fun selectUserDataByUserId(userId:String) : MutableList<UserVO>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val result = collectionReference.whereEqualTo("userId", userId).get().await()
            val userVoList = result.toObjects(UserVO::class.java)
            return userVoList
        }

        // 사용자 이름을 통해 사용자 데이터를 가져오는 메서드
        suspend fun selectUserDataByUserName(userName:String) : MutableList<UserVO>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val result = collectionReference.whereEqualTo("userName", userName).get().await()
            val userVoList = result.toObjects(UserVO::class.java)
            return userVoList
        }

    }
}