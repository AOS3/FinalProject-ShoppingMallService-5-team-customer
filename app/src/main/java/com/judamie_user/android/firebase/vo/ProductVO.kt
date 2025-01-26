package com.judamie_user.android.firebase.vo

import com.judamie_user.android.firebase.model.ProductModel
import com.judamie_user.android.util.ProductState

class ProductVO {
    var productName	= ""
    var productCategory = ""
    var productPrice = 0
    var productDiscountRate	= 0
    var productStock = 0
    var productDescription = ""
    var productMainImage = ""
    var productSubImage: List<String> = emptyList()
    var productReview= mutableListOf<String>()
    var productSeller = ""
    var productState = 0
    var productRegisterDate	= ""
    var productReview : List<String> = emptyList()
    var productSeller = ""
    var productTimeStamp = 0L

    fun toProductModel(productDocumentId:String) : ProductModel {
        val productModel = ProductModel()

        productModel.productDocumentId = productDocumentId
        productModel.productName = productName
        productModel.productCategory = productCategory
        productModel.productPrice = productPrice
        productModel.productDiscountRate = productDiscountRate
        productModel.productDescription = productDescription
        productModel.productStock = productStock
        productModel.productMainImage = productMainImage
        productModel.productRegisterDate = productRegisterDate
        productModel.productSeller = productSeller
        productModel.productTimeStamp = productTimeStamp

        when (productState){
            ProductState.PRODUCT_NORMAL.number -> productModel.productState = ProductState.PRODUCT_NORMAL
            ProductState.PRODUCT_DELETE.number -> productModel.productState = ProductState.PRODUCT_DELETE
        }

        productModel.productSubImage = productSubImage.toMutableList()
        productModel.productReview = productReview.toMutableList()

        productModel.productReview = productReview

        productModel.productSeller = productSeller

        return productModel
    }
}