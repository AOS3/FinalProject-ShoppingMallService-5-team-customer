package com.judamie_user.android.util

// 카테고리 구분 값
enum class ProductType(val number:Int, val str:String){
    PRODUCT_TYPE_ALL(0, "전체"),
    PRODUCT_TYPE_WINE(1, "와인"),
    PRODUCT_TYPE_WHISKEY(2, "위스키"),
    PRODUCT_TYPE_VODKA(3, "보드카"),
    PRODUCT_TYPE_TEQUILA(4, "데낄라"),
    PRODUCT_TYPE_DOMESTIC(5, "우리술"),
    PRODUCT_TYPE_SAKE(6, "사케"),
    PRODUCT_TYPE_RUM(7, "럼"),
    PRODUCT_TYPE_LIQUEUR(8, "리큐르"),
    PRODUCT_TYPE_CHINESE(9, "중국술"),
    PRODUCT_TYPE_BRANDY(10, "브랜디"),
    PRODUCT_TYPE_BEER(11, "맥주"),
    PRODUCT_TYPE_NON_ALCOHOL(12, "논알콜")
}

enum class ProductState(val number:Int, val str:String){
    // 정상
    PRODUCT_STATE_NORMAL(1, "정상"),
    // 삭제
    PRODUCT_STATE_DELETE(2, "삭제"),
}

// 쿠폰 사용 여부를 나타내는 값
enum class CouponUsableType(var num:Int, var str: String){
    // 사용 가능
    COUPON_USABLE(1, "사용 가능"),
    // 사용 불가능
    COUPON_UNUSABLE(2, "사용 불가능")
}

// 픽업지 상태를 나타내는 값
enum class PickupStateType(var num:Int, var str: String){
    // 기본
    PICKUP_STATE_NORMAL(1, "정상"),
    // 삭제
    PICKUP_STATE_DELETE(2, "삭제")
}

