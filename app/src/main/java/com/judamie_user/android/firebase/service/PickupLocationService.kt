package com.judamie_user.android.firebase.service

import android.util.Log
import com.judamie_manager.firebase.model.CouponModel
import com.judamie_manager.firebase.vo.CouponVO
import com.judamie_manager.firebase.vo.PickupLocationVO
import com.judamie_user.android.firebase.model.PickupLocationModel
import com.judamie_user.android.firebase.repository.CouponRepository
import com.judamie_user.android.firebase.repository.PickupLocationRepository

class PickupLocationService {
    companion object{
        // 모든 픽업지 데이터를 가져오는 메서드
        suspend fun gettingAllPickLocations(): MutableList<PickupLocationModel> {
            val PickupLocationVOList: MutableList<PickupLocationModel> = PickupLocationRepository.gettingPickupLocationList()
            return PickupLocationVOList
        }

        // 유저의 픽업지 정보를 가져오는 메서드
        suspend fun gettingUserPickupLocation(userDocumentID: String): String? {
            val userPickLocationInfo = PickupLocationRepository.gettingUserPickupLocation(userDocumentID)
            return userPickLocationInfo
        }

        // 유저의 픽업지 정보를 설정하는 함수
        suspend fun settingUserPickupLocation(userDocumentID: String, pickLocationDocumentID: String) {
            PickupLocationRepository.settingUserPickupLocation(userDocumentID,pickLocationDocumentID)
        }

        // 픽업지 아이디로 픽업지 모델을 가져온다
        suspend fun gettingPickupLocationById(pickLocationDocumentID: String): PickupLocationModel {
            // VO와 Document ID를 가져옴
            val pickupLocationVO = PickupLocationRepository.gettingPickupLocationById(pickLocationDocumentID)

            val pickupLocationModel = pickupLocationVO.toPickupLocationModel(pickLocationDocumentID)

            Log.d("test2",pickupLocationModel.pickupLocName)

            return pickupLocationModel
        }



    }
}