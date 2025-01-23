package com.judamie_user.android.firebase.model

import com.judamie_user.android.firebase.vo.ProductVO
import com.judamie_user.android.util.ProductState


class ProductModel {
    var productDocumentIdD = ""
    var productDocumentId = ""
    var productName= ""
    var productCategory = ""
    var productPrice = 0
    var productDiscountRate= 0
    var productDescription = ""
    var productStock = 0
    var productMainImage = ""
    var productSubImage: List<String> = emptyList()
    var productState = ProductState.PRODUCT_NORMAL
    var productRegisterDate= ""
    var productReview: List<String> = mutableListOf()
    var productSeller = ""
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
        productVO.productSeller = productSeller
        productVO.productTimeStamp = productTimeStamp

        productVO.productSubImage = productSubImage.toMutableList()
        productVO.productReview = productReview.toMutableList()

        return productVO
    }
}