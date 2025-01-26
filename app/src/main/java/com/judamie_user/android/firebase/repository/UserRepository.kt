package com.judamie_user.android.firebase.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.judamie_user.android.firebase.vo.ProductVO
import kotlinx.coroutines.tasks.await

class UserRepository {
    companion object {

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


//        suspend fun getProductsFromWishList(userDocumentID: String): List<ProductVO> {
//            val db = FirebaseFirestore.getInstance()
//
//            // Step 1: User's wish list 가져오기
//            val document = db.collection("UserData")
//                .document(userDocumentID)
//                .get()
//                .await()
//            val wishList = document.get("userWishList") as? List<String> ?: emptyList()
//
//            // Step 2: Wish list ID로 물건 조회 및 ID 추가
//            val productList = mutableListOf<ProductVO>()
//            for (productId in wishList) {
//                val productDocument = db.collection("productData")
//                    .document(productId)
//                    .get()
//                    .await()
//
//                productDocument.toObject(ProductVO::class.java)?.let { productVO ->
//                    // Document ID를 설정
//                    productVO.toProductModel(productDocument.id)?.let { product ->
//                        productList.add(productVO)
//                    }
//                }
//            }
//            return productList
//        }



    }
}