package com.judamie_user.android.firebase.vo

import com.judamie_user.android.firebase.model.UserModel
import com.judamie_user.android.util.UserState

// 고객 VO
class UserVO {
    // 유저 아이디
    var userId = ""
    // 유저 비밀번호
    var userPassword = ""
    // 유저 이름
    var userName = ""
    // 유저 전화번호
    var userPhoneNumber = ""
    // 찜 목록 (productDocumentID 리스트)
    var userWishList = mutableListOf<String>()
    // 장바구니 목록 (productDocumentID 리스트)
    var userCartList = mutableListOf<String>()
    // 유저가 지정한 픽업지
    var userPickupLoc: String = ""
    // 알림 설정 여부
    var userNotificationAllow: Boolean = false
    // 자동 로그인 토큰
    var userAutoLoginToken = ""
    // 탈퇴 여부 (기본: 1, 탈퇴: 2)
    // Values.UserState
    var userState = 0
    // 유저가 가진 쿠폰 리스트 (couponDocumentID)
    var userCoupons = mutableListOf<String>()
    // 데이터가 저장된 시간 (TimeStamp, nano)
    var userTimeStamp: Long = 0

    fun toUserModel(userDocumentId:String) : UserModel {
        val userModel = UserModel()

        userModel.userDocumentID = userDocumentId
        userModel.userId = userId
        userModel.userPassword = userPassword
        userModel.userAutoLoginToken = userAutoLoginToken
        userModel.userName = userName
        userModel.userTimeStamp = userTimeStamp
        userModel.userNotificationAllow = userNotificationAllow

        when(userState){
            UserState.USER_STATE_NORMAL.number -> userModel.userState = UserState.USER_STATE_NORMAL
            UserState.USER_STATE_SIGN_OUT.number -> userModel.userState = UserState.USER_STATE_SIGN_OUT
        }

        userModel.userWishList = userWishList.toMutableList()
        userModel.userCartList = userCartList.toMutableList()
        userModel.userCoupons = userCoupons.toMutableList()


        return userModel
    }
}

