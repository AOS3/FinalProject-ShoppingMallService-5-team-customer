package com.judamie_user.android.firebase.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.judamie_user.android.firebase.vo.ProductVO
import com.judamie_user.android.util.ProductCategory
import kotlinx.coroutines.tasks.await

class ProductRepository {

    companion object {
        // 제품 목록을 가져오는 메서드
        suspend fun gettingProductList(productCategory: ProductCategory) : MutableList<Map<String, *>>{
            val firestore = FirebaseFirestore.getInstance()
            // TODO (change collection name : productData -> ProductData)
            val collectionReference = firestore.collection("productData")
            val result = if (productCategory == ProductCategory.PRODUCT_CATEGORY_DEFAULT) {
                collectionReference.orderBy("productTimeStamp", Query.Direction.DESCENDING).get().await()

            } else {
                collectionReference.whereEqualTo("productCategory", productCategory.str)
                    .orderBy("productTimeStamp", Query.Direction.DESCENDING).get().await()

            }
            // 반환할 리스트
            val resultList = mutableListOf<Map<String, *>>()
            // 데이터의 수 만큼 반환한다.
            result.forEach {
                val map = mapOf(
                    // 제품의 Document Id
                    "documentId" to it.id,
                    // 데이터를 가지고 있는 객체
                    "productVO" to it.toObject(ProductVO::class.java)
                )
                resultList.add(map)
            }

            //Log.d("test100", " ${resultList.size} 카테고리 항목: ${productCategory.str}")

            return resultList
        }

        // 이미지 데이터를 가져온다.
        suspend fun gettingImage(imageFileName:String) : Uri {
            val storageReference = FirebaseStorage.getInstance().reference
            // 파일명을 지정하여 이미지 데이터를 가져온다.
            val childStorageReference = storageReference.child("image/$imageFileName")
            val imageUri = childStorageReference.downloadUrl.await()
            return imageUri
        }

    }


}