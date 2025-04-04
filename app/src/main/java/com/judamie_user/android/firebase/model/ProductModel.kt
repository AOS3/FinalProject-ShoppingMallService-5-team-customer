package com.judamie_user.android.firebase.model

import com.judamie_user.android.firebase.vo.ProductVO
import com.judamie_user.android.util.ProductState

class ProductModel {
    var productDocumentId = ""
    var productName= ""
    var productCategory = ""
    var productPrice:Double = 0.0
    var productDiscountRate= 0
    var productDescription = ""
    var productStock = 0
    var productMainImage = ""
    var productSubImage: List<String> = emptyList()
    var productReview= mutableListOf<String>()
    var productSeller = ""
    var productState = ProductState.PRODUCT_NORMAL
    var productRegisterDate= ""
    var productTimeStamp = 0L

    fun toProductVO() : ProductVO {
        val productVO = ProductVO()
        productVO.productName = productName
        productVO.productCategory = productCategory
        productVO.productPrice = productPrice
        productVO.productDiscountRate = productDiscountRate
        productVO.productDescription = productDescription
        productVO.productStock = productStock
        productVO.productMainImage = productMainImage
        productVO.productState = productState.number
        productVO.productRegisterDate = productRegisterDate
        productVO.productTimeStamp = productTimeStamp

        productVO.productSubImage = mutableListOf()
        productVO.productReview = productReview
        productVO.productSeller = productSeller

        return productVO
    }
}