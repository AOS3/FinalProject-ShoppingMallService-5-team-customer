package com.judamie_user.android.util

//주류 카테고리
enum class ProductCategory(val number:Int,val str:String){
    //기본값
    PRODUCT_CATEGORY_DEFAULT(0,"전체"),
    //와인
    PRODUCT_CATEGORY_WINE(1,"와인"),
    //위스키
    PRODUCT_CATEGORY_WHISKEY(2,"위스키"),
    //보드카
    PRODUCT_CATEGORY_VODKA(3,"보드카"),
    //데낄라
    PRODUCT_CATEGORY_TEQUILA(4,"데낄라"),
    //우리술
    PRODUCT_CATEGORY_TRADITIONAL_LIQUOR(5,"우리술"),
    //사케
    PRODUCT_CATEGORY_SAKE(6,"사케"),
    //럼
    PRODUCT_CATEGORY_RUM(7,"럼"),
    //리큐르
    PRODUCT_CATEGORY_LIQUEUR(8,"리큐르"),
    //중국술
    PRODUCT_CATEGORY_CHINA_LIQUOR(9,"중국술"),
    //브랜디
    PRODUCT_CATEGORY_BRANDY(10,"브랜디"),
    //맥주
    PRODUCT_CATEGORY_BEER(11,"맥주"),
    //논알콜
    PRODUCT_CATEGORY_NON_ALC(12,"논알콜")
}

//상품 상태(품절과 무관하고 판매자가 제품을 삭제했을때 2로바뀜)
enum class ProductState(val number:Int,val str:String){
    // 상품 팔고있음
    PRODUCT_NORMAL(1,"팔고있음"),
    // 상품 팔지않음
    PRODUCT_DELETE(2,"팔지않음");

    companion object {
        // 숫자를 받아서 ProductState로 변환
        fun fromNumber(number: Int): ProductState {
            return values().find { it.number == number }
                ?: PRODUCT_NORMAL // 기본값 설정
        }
    }

}

// 픽업지 상태를 나타내는 값
enum class PickupStateType(var num:Int, var str: String){
    // 기본
    PICKUP_STATE_NORMAL(1, "정상"),
    // 삭제
    PICKUP_STATE_DELETE(2, "삭제")
}

// 쿠폰 사용 여부를 나타내는 값
enum class CouponUsableType(var num:Int, var str: String){
    // 사용 가능
    COUPON_USABLE(1, "사용 가능"),
    // 사용 불가능
    COUPON_UNUSABLE(2, "사용 불가능")
}

// 사용자 상태 값
enum class UserState(val number:Int, val str:String){
    // 정상
    USER_STATE_NORMAL(1, "정상"),
    // 탈퇴
    USER_STATE_SIGN_OUT(2, "탈퇴")
}

// 로그인 결과 값
enum class LoginResult(val number: Int, val str: String) {
    LOGIN_RESULT_SUCCESS(1, "로그인 성공"),
    LOGIN_RESULT_ID_NOT_EXIST(2, "존재하지 않는 아이디"),
    LOGIN_RESULT_PASSWORD_INCORRECT(3, "잘못된 비밀번호"),
    LOGIN_RESULT_SIGN_OUT_MEMBER(4, "탈퇴한 회원"),
}

