package com.judamie_user.android.firebase.service

import android.net.Uri
import com.judamie_user.android.firebase.model.ProductModel
import com.judamie_user.android.firebase.repository.ProductRepository
import com.judamie_user.android.firebase.vo.ProductVO
import com.judamie_user.android.firebase.vo.UserVO
import com.judamie_user.android.util.ProductCategory

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

        // userCartList에 포함된 상품 ID를 기반으로 상품 정보를 가져오기
        suspend fun gettingCartList(userCartList: List<String>) : List<ProductModel>{
            return ProductRepository.gettingCartList(userCartList)
        }

        // 이미지 데이터를 가져온다.
        suspend fun gettingImage(imageFileName:String) : Uri {
            val imageUri = ProductRepository.gettingImage(imageFileName)
            return imageUri
        }

    }
}