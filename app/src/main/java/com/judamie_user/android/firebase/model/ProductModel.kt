package com.judamie_user.android.firebase.model

import com.judamie_user.android.firebase.vo.ProductVO
import com.judamie_user.android.util.ProductCategory
import com.judamie_user.android.util.ProductState

class ProductModel {
    //productDocumentID	int	주류 기본키
    val productDocumentID = ""

    //productName	String	주류 이름
    val productName = ""

    //productCategory	String	주류 카테고리 enum class
    val productCategory = ProductCategory.PRODUCT_CATEGORY_DEFAULT

    //productPrice	Int	주류가격
    val productPrice = ""

    //productDiscountRate	Int	할인율
    val productDiscountRate = ""

    //productDescription	String	주류설명
    val productDescription = ""

    //productInventory	int	재고량
    val productInventory = ""

    //productMainImage	String	대표이미지
    val productMainImage = ""

    //productSubImage	List<String> 추가이미지
    val productSubImage = mutableListOf<String>()

    //productState	Int	상품 상태 enum class
    val productState = ProductState.PRODUCT_NORMAL

    //productRegisterDate	String	상품등록날짜
    val productRegisterDate = ""

    //productTimeStamp	Int	데이터 들어온시간
    val productTimeStamp = ""

    fun toProductVO(): ProductVO {
        val productVO = ProductVO()
        productVO.productName = productName
        productVO.productCategory = productCategory.str
        productVO.productPrice = productPrice
        productVO.productDiscountRate = productDiscountRate
        productVO.productDescription = productDescription
        productVO.productInventory = productInventory
        productVO.productMainImage = productMainImage
        productVO.productSubImage = mutableListOf()
        productSubImage.forEach {
            productSubImage.add(it)
        }
        productVO.productState = productState.str
        productVO.productRegisterDate = productRegisterDate
        productVO.productTimeStamp = productTimeStamp

        return productVO
    }
}