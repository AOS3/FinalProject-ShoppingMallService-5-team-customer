package com.judamie_user.android.firebase.model

import com.judamie_user.android.firebase.vo.UserVO
import com.judamie_user.android.util.UserState

class UserModel {
    // 유저 기본키 (Document ID)
    var userDocumentID = ""
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
    var userState = UserState.USER_STATE_NORMAL
    // 유저가 가진 쿠폰 리스트 (couponDocumentID)
    var userCoupons = mutableListOf<String>()
    // 데이터가 저장된 시간 (TimeStamp, nano)
    var userTimeStamp = 0L

    fun toUserVO() : UserVO {
        val userVO = UserVO()
        userVO.userId = userId
        userVO.userPassword = userPassword
        userVO.userAutoLoginToken = userAutoLoginToken
        userVO.userName = userName
        userVO.userPhoneNumber = userPhoneNumber
        userVO.userTimeStamp = userTimeStamp
        userVO.userPickupLoc = userPickupLoc
        userVO.userNotificationAllow = userNotificationAllow

        userVO.userState = userState.number

        userVO.userWishList = userWishList.toMutableList()
        userVO.userCartList = userCartList.toMutableList()
        userVO.userCoupons = userCoupons.toMutableList()

        return userVO
    }
}