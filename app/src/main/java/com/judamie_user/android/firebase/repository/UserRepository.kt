package com.judamie_user.android.firebase.repository

import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.judamie_user.android.firebase.vo.UserVO
import com.judamie_user.android.util.UserState
import kotlinx.coroutines.tasks.await

class UserRepository {
    companion object{

        // 사용자 정보를 추가하는 메서드
        fun addUserData(userVO: UserVO){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            collectionReference.add(userVO)
        }

        // 유저의 장바구니 리스트 삭제하는 메서드
        suspend fun deleteCartItem(userDocumentId: String, productDocumentId: String) {
            try {
                val cartRef = FirebaseFirestore.getInstance()
                    .collection("UserData")
                    .document(userDocumentId)


                val snapshot = cartRef.get().await()
                if (snapshot.exists()) {
                    val cartItems = snapshot.get("userCartList") as? MutableList<String>
                    if (cartItems != null) {
                        // productDocumentId와 일치하는 아이템 삭제
                        val updatedCartItems = cartItems.filterNot { it == productDocumentId }

                        cartRef.update("userCartList", updatedCartItems).await()
                        Log.d("CartService", "장바구니에서 상품 삭제 완료: $productDocumentId")
                    }
                }

            } catch (e: Exception) {
                Log.e("CartService", "장바구니 상품 삭제 오류: ${e.message}")
            }
        }

        // 장바구이에 상품 추가하는 메서드
        suspend fun addCartData(userVO: UserVO, userDocumentID:String){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val documentReference = collectionReference.document(userDocumentID)

            // 수정할 데이터를 맵에 담는다
            // 데이터의 이름을 필드의 이름으로 해준다.
            val updateMap = mapOf(
                "userCartList" to userVO.userCartList,
            )
            // 수정한다.
            documentReference.update(updateMap).await()
        }

//        // 장바구니 상품 삭제하는 메서드
//        suspend fun deleteUserCartData(userDocumentId: String, selectedIds: List<String>){
//            val firestore = FirebaseFirestore.getInstance()
//            val collectionReference = firestore.collection("UserData")
//            val documentReference = collectionReference.document(userDocumentId)
//
//            // Firebase에서 유저 문서를 가져오고, cartList에서 삭제할 상품을 제거
//            val userDoc = documentReference.get().await()
//            val userVO = userDoc.toObject(UserVO::class.java)
//
//            userVO?.let {
//                // 선택된 상품 아이디를 제거
//                it.userCartList.removeAll { productId -> selectedIds.contains(productId) }
//
//                // cartList 갱신된 데이터로 업데이트
//                documentReference.set(it).await()
//            }
//
//        }

        // 장바구니 상품 삭제하는 메서드
        suspend fun deleteUserCartData(userDocumentId: String, selectedIds: String) {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val documentReference = collectionReference.document(userDocumentId)

            try {
                // Firestore에서 유저 문서를 가져오기 전에 로그로 데이터를 확인
                Log.d("Firestore", "Fetching user data for document: $userDocumentId")

                val userDoc = documentReference.get().await()

                if (!userDoc.exists()) {
                    Log.e("FirestoreError", "User document does not exist.")
                    return
                }

                val userVO = userDoc.toObject(UserVO::class.java)
                Log.d("Firestore", "Fetched user data: $userVO")
                Log.d("Firestore", "Selected IDs to delete: $selectedIds")


                userVO?.let {
                    Log.d("Firestore", "User cart list before removal: ${it.userCartList}")

                    // 선택된 상품 아이디를 제거
                    val initialSize = it.userCartList.size
                    it.userCartList.removeAll { productId -> selectedIds.contains(productId) }

                    Log.d("Firestore", "User cart list after removal: ${it.userCartList}")

                    // 삭제된 아이템 수가 0보다 크다면 업데이트를 진행
                    if (it.userCartList.size < initialSize) {
                        // cartList 갱신된 데이터로 업데이트
                        documentReference.set(it).await()

                        // 업데이트 후 다시 데이터를 가져와서 로그 확인
                        val updatedUserDoc = documentReference.get().await()
                        val updatedUserVO = updatedUserDoc.toObject(UserVO::class.java)
                        Log.d("Firestore", "Updated user data: $updatedUserVO")
                    } else {
                        Log.d("Firestore", "No items to delete.")
                    }
                } ?: run {
                    Log.e("FirestoreError", "User data is null.")
                }
            } catch (e: Exception) {
                // 에러가 발생하면 로그 출력
                Log.e("FirestoreError", "Failed to delete cart data: ${e.message}")
            }
        }



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

        // 사용자 아이디와 동일한 사용자의 정보 하나를 반환하는 메서드
        suspend fun selectUserDataByUserIdOne(userId:String) : MutableMap<String, *>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val result = collectionReference.whereEqualTo("userId", userId).get().await()
            val userVoList = result.toObjects(UserVO::class.java)

            val userMap = mutableMapOf(
                "user_document_id" to result.documents[0].id,
                "user_vo" to userVoList[0]
            )
            return userMap
        }

        // 사용자 정보 전체를 가져오는 메서드
        suspend fun selectUserDataAll() : MutableList<MutableMap<String, *>>{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val result = collectionReference.get().await()
            val userList = mutableListOf<MutableMap<String, *>>()
            result.forEach {
                val userMap = mutableMapOf(
                    "user_document_id" to it.id,
                    "user_vo" to it.toObject(UserVO::class.java)
                )
                userList.add(userMap)
            }
            return userList
        }

        // 사용자 Document Id를 통해 사용자 정보를 가져오는 메서드
        suspend fun selectUserDataByUserDocumentIdOne(userDocumentId:String) : UserVO{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val result = collectionReference.document(userDocumentId).get().await()
            val userVO = result.toObject(UserVO::class.java)!!

            return userVO
        }

        // 자동 로그인 토큰값 갱신 메서드
        suspend fun updateUserAutoLoginToken(userDocumentId:String, newToken:String){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val documentReference = collectionReference.document(userDocumentId)
            val tokenMap = mapOf(
                "userAutoLoginToken" to newToken
            )
            documentReference.update(tokenMap).await()
        }

        // 자동 로그인 토큰 값으로 사용자 정보를 가져오는 메서드
        suspend fun selectUserDataByLoginToken(loginToken:String) : Map<String, *>?{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val resultList = collectionReference.whereEqualTo("userAutoLoginToken", loginToken).get().await()
            val userVOList = resultList.toObjects(UserVO::class.java)
            if(userVOList.isEmpty()){
                return null
            } else {
                val userDocumentId = resultList.documents[0].id
                val returnMap = mapOf(
                    "userDocumentId" to userDocumentId,
                    "userVO" to userVOList[0]
                )
                return returnMap
            }
        }
        suspend fun gettingUserNameByID(userDocumentID: String): String {
            val db = FirebaseFirestore.getInstance()

            val document = db.collection("UserData")
                .document(userDocumentID)
                .get()
                .await() // await()을 사용하여 비동기 Task를 처리


            return document.getString("userName") ?: "Unknown User"

        }

        suspend fun gettingWishListByUserID(userDocumentID: String): List<String> {
            val db = FirebaseFirestore.getInstance()

            // Step 1: User's wish list 가져오기
            val document = db.collection("UserData")
                .document(userDocumentID)
                .get()
                .await()
            val wishList = document.get("userWishList") as? MutableList<String> ?: emptyList()

            return wishList
        }

        //해당 유저의 위시리스트에서 매개변수로들어온 제품을 추가하는메서드
        suspend fun addUserWishList(userDocumentID: String, productDocumentID: String) {
            val db = FirebaseFirestore.getInstance()

            // Step 1: User's wish list 가져오기
            val document = db.collection("UserData")
                .document(userDocumentID)
                .get()
                .await()

            // Step 2: userWishList 필드 가져오기 (기존 값)
            val currentWishList = document.get("userWishList") as? MutableList<String> ?: mutableListOf()

            // Step 3: 제품 ID를 리스트에 추가 (중복 방지)
            if (!currentWishList.contains(productDocumentID)) {
                currentWishList.add(productDocumentID)

                // Step 4: 업데이트된 wish list를 Firestore에 저장
                db.collection("UserData")
                    .document(userDocumentID)
                    .update("userWishList", currentWishList)
                    .await()
            }
        }

        //해당 유저의 위시리스트에서 매개변수로들어온 제품을 삭제하는메서드
        suspend fun deleteUserWishList(userDocumentID: String, productDocumentID: String) {
            val db = FirebaseFirestore.getInstance()

            // Step 1: User's wish list 가져오기
            val document = db.collection("UserData")
                .document(userDocumentID)
                .get()
                .await()

            // Step 2: userWishList 필드 가져오기 (기존 값)
            val currentWishList = document.get("userWishList") as? MutableList<String> ?: mutableListOf()

            // Step 3: 제품 ID 삭제
            if (currentWishList.contains(productDocumentID)) {
                currentWishList.remove(productDocumentID)

                // Step 4: 업데이트된 wish list를 Firestore에 저장
                db.collection("UserData")
                    .document(userDocumentID)
                    .update("userWishList", currentWishList)
                    .await()
            }
        }

        // 탈퇴처리(해당 유저의 상태값을 변경한다)
        suspend fun updateUserState(userDocumentId:String, newState: UserState){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("UserData")
            val documentReference = collectionReference.document(userDocumentId)

            val updateMap = mapOf(
                "userState" to newState.number
            )

            documentReference.update(updateMap).await()
        }

        // 서버에서 쿠폰을 삭제하는 메서드
        suspend fun deleteCouponFromUser(userDocumentId: String, couponDocumentId: String) {
            // 유저 데이터 가져오기
            val userVO = selectUserDataByUserDocumentIdOne(userDocumentId)

            // userCoupons 리스트에서 해당 쿠폰 ID 삭제
            val updatedCoupons = userVO.userCoupons.toMutableList().apply {
                remove(couponDocumentId)
            }

            // Firestore 업데이트
            updateUserCoupons(userDocumentId, updatedCoupons)
        }

        // 쿠폰 목록 업데이트(수정) 메서드
        suspend fun updateUserCoupons(userDocumentId: String, updatedCoupons: List<String>) {
            val firestore = FirebaseFirestore.getInstance()
            val userRef = firestore.collection("UserData").document(userDocumentId)

            userRef.update("userCoupons", updatedCoupons).await()
        }

    }
}