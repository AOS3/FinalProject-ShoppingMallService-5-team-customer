package com.judamie_user.android.firebase.model

import com.judamie_manager.firebase.vo.CouponVO
import com.judamie_manager.firebase.vo.PickupLocationVO
import com.judamie_user.android.util.PickupStateType


class PickupLocationModel {
    // 픽업지 id
    var pickupLocDocumentID = ""
    // 픽업지 이름
    var pickupLocName = ""
    // 픽업지 도로명 주소
    var pickupLocStreetAddress = ""
    // 픽업지 상세 주소
    var pickupLocAddressDetail = ""
    // 픽업지 전화번호
    var pickupLocPhoneNumber = ""
    // 픽업지 추가 사항
    var pickupLocInformation = ""
    // 픽업지 상태
    var pickupLocState = PickupStateType.PICKUP_STATE_NORMAL.num
    // 시간
    var pickupLocTimeStamp = 0L

    fun toPickupLocationVO(): PickupLocationVO {
        val pickupLocationVO = PickupLocationVO()
        pickupLocationVO.pickupLocName = pickupLocName
        pickupLocationVO.pickupLocStreetAddress = pickupLocStreetAddress
        pickupLocationVO.pickupLocAddressDetail = pickupLocAddressDetail
        pickupLocationVO.pickupLocState = pickupLocState
        pickupLocationVO.pickupLocPhoneNumber = pickupLocPhoneNumber
        pickupLocationVO.pickupLocInformation = pickupLocInformation
        pickupLocationVO.pickupLocTimeStamp = pickupLocTimeStamp

        return pickupLocationVO
    }
}