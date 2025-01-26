package com.judamie_user.android.firebase.service

import android.net.Uri
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.judamie_user.android.firebase.model.ProductModel
import com.judamie_user.android.firebase.repository.ProductRepository
import com.judamie_user.android.firebase.repository.UserRepository
import com.judamie_user.android.firebase.vo.ProductVO
import com.judamie_user.android.firebase.vo.UserVO
import com.judamie_user.android.util.ProductCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class ProductService {

    companion object {

        // 상품 목록을 가져오는 메서드
        suspend fun gettingProductList(productCategory: ProductCategory) : MutableList<ProductModel>{
            // 상품 정보를 가져온다.
            val productList = mutableListOf<ProductModel>()
            val resultList = ProductRepository.gettingProductList(productCategory)

            // 사용자 정보를 가져온다.
            val userList = UserRepository.selectUserDataAll()
            // 사용자 정보를 맵에 담는다.
            val userMap = mutableMapOf<String, String>()
            userList.forEach {
                val userDocumentId = it["user_document_id"] as String
                val userVO = it["user_vo"] as UserVO
                userMap[userDocumentId] = userVO.userName
            }

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

        // 이미지 데이터들을 가져온다.
        // 비동기적으로 이미지 URI들을 가져오는 함수
        suspend fun getImageUris(productList: List<ProductModel>) : MutableList<Uri> {
            return coroutineScope {
                // 비동기적으로 이미지 URI를 가져오는 작업을 리스트로 모은다
                val deferredList = productList.mapIndexed { index, product ->
                    async(Dispatchers.IO) {
                        // 각 제품에 대한 이미지 URI를 비동기적으로 가져온다
                        gettingImage(product.productMainImage)  // 서버에서 이미지 URI를 가져오는 함수
                    }
                }

                // 결과를 리스트로 반환
                deferredList.awaitAll().toMutableList()
            }
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

        // 제품 하나의 정보를 가져오는 메서드 (특정 필드만 가져오기)
        suspend fun gettingProductName(documentID: String): String? {
            val productName = ProductRepository.gettingProductName(documentID)
            return productName
        }
        // 상품 id받아서 서브 이미지리스트 받아오기
        suspend fun gettingSubImage(productDocumentId: String): List<Uri> {
            val documentSnapshot = ProductRepository.gettingSubImage(productDocumentId)

            // Firestore에서 받은 문서가 null이 아니면 productSubImage 필드를 가져오기
            return if (documentSnapshot != null) {
                // val subImages = documentSnapshot.get("productSubImage") as? List<String> ?: emptyList()

                val subImageFileNames = documentSnapshot.get("productSubImage") as? List<String> ?: emptyList()

                // 각 이미지 파일명에 대해 Firebase Storage에서 실제 이미지 URI를 가져오기
                subImageFileNames.map { fileName ->
                    // Firebase Storage에서 이미지 URI를 가져오는 함수 호출
                    gettingImage(fileName)
                }// 이미지 URI를 가져오는 함수 호출
            } else {
                // 문서가 없으면 빈 리스트 반환
                emptyList()
            }
        }
    }
}