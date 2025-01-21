package com.judamie_manager.firebase.vo

import com.judamie_user.android.firebase.model.PickupLocationModel
import com.judamie_user.android.util.PickupStateType

class PickupLocationVO {
    // 픽업지 이름
    var pickupLocName = ""
    // 픽업지 도로명 주소
    var pickupLocStreetAddress = ""
    // 픽업지 상세 주소
    var pickupLocAddressDetail = ""
    // 픽업지 전화번호
    var pickupLocPhoneNumber = ""
    // 픽업지 추가 사항
    var pickupLocInfomation = ""
    // 픽업지 상태
    var pickupLocState = 1
    // 시간
    var pickupLocTimeStamp = 0L

    fun toPickupLocationModel(pickupLocDocumentID:String) : PickupLocationModel {
        val pickupLocationModel = PickupLocationModel()

        pickupLocationModel.pickupLocDocumentID = pickupLocDocumentID
        pickupLocationModel.pickupLocName = pickupLocName
        pickupLocationModel.pickupLocStreetAddress = pickupLocStreetAddress
        pickupLocationModel.pickupLocAddressDetail = pickupLocAddressDetail
        pickupLocationModel.pickupLocPhoneNumber = pickupLocPhoneNumber
        pickupLocationModel.pickupLocInfomation = pickupLocInfomation
        pickupLocationModel.pickupLocTimeStamp = pickupLocTimeStamp


        when(pickupLocState){
            PickupStateType.PICKUP_STATE_NORMAL.num -> pickupLocationModel.pickupLocState = PickupStateType.PICKUP_STATE_NORMAL
            PickupStateType.PICKUP_STATE_DELETE.num -> pickupLocationModel.pickupLocState = PickupStateType.PICKUP_STATE_DELETE
        }

        return pickupLocationModel
    }
}
