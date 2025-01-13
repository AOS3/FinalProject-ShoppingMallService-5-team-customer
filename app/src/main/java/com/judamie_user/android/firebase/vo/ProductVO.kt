package com.judamie_user.android.firebase.vo

class ProductVO {

    //productName	String	주류 이름
    var productName = ""

    //productCategory	String	주류 카테고리 enum class
    var productCategory = ""

    //productPrice	Int	주류가격
    var productPrice = ""

    //productDiscountRate	Int	할인율
    var productDiscountRate = ""

    //productDescription	String	주류설명
    var productDescription = ""

    //productInventory	int	재고량
    var productInventory = ""

    //productMainImage	String	대표이미지
    var productMainImage = ""

    //productSubImage	List<String> 추가이미지
    var productSubImage = mutableListOf<String>()

    //productState	Int	상품 상태 enum class
    var productState = ""

    //productRegisterDate	String	상품등록날짜
    var productRegisterDate = ""

    //productTimeStamp	Int	데이터 들어온시간
    var productTimeStamp = ""
}