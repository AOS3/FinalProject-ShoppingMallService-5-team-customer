package com.judamie_user.android.firebase.service

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.judamie_user.android.firebase.model.ProductModel
import com.judamie_user.android.firebase.repository.ProductRepository
import com.judamie_user.android.firebase.vo.ProductVO
import com.judamie_user.android.util.ProductCategory
import kotlinx.coroutines.tasks.await

class ProductService {

    companion object {

        // 상품 목록을 가져오는 메서드
        suspend fun gettingProductList(productCategory: ProductCategory) : MutableList<ProductModel>{
            // 상품 정보를 가져온다.
            val productList = mutableListOf<ProductModel>()
            val resultList = ProductRepository.gettingProductList(productCategory)
            // 사용자 정보를 가져온다.
            // TODO
//            val userList = UserRepository.selectUserDataAll()
//            // 사용자 정보를 맵에 담는다.
//            val userMap = mutableMapOf<String, String>()
//            userList.forEach {
//                val userDocumentId = it["user_document_id"] as String
//                val userVO = it["user_vo"] as UserVO
//                userMap[userDocumentId] = userVO.userName
//            }

            resultList.forEach {
                val productVO = it["productVO"] as ProductVO
                val documentId = it["documentId"] as String
                val productModel = productVO.toProductModel(documentId)
                productList.add(productModel)
            }

            return productList
        }

        // 이미지 데이터를 가져온다.
        suspend fun gettingImage(imageFileName:String) : Uri {
            val imageUri = ProductRepository.gettingImage(imageFileName)
            return imageUri
        }

        // 제품 하나의 정보를 가져오는 메서드
        suspend fun gettingProductOne(documentID:String) : ProductModel{
            val productVO = ProductRepository.gettingProductOne(documentID)
            val productModel = productVO.toProductModel(documentID)

            return productModel
        }

    }
}