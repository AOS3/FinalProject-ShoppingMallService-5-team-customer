package com.judamie_user.android.firebase.service

import com.judamie_manager.firebase.model.CouponModel
import com.judamie_manager.firebase.vo.CouponVO
import com.judamie_manager.firebase.vo.PickupLocationVO
import com.judamie_user.android.firebase.model.PickupLocationModel
import com.judamie_user.android.firebase.repository.CouponRepository
import com.judamie_user.android.firebase.repository.PickupLocationRepository

class PickupLocationService {
    companion object{
        // 모든 쿠폰 데이터를 가져오는 메서드
        suspend fun gettingAllPickLocations(): MutableList<PickupLocationModel> {
            val PickupLocationVOList: MutableList<PickupLocationVO> = PickupLocationRepository.gettingPickupLocationList()
            return PickupLocationVOList.map { PickupLocationVO ->
                val pickupLocationDocumentID = PickupLocationVO.pickupLocName
                PickupLocationVO.toPickupLocationModel(pickupLocationDocumentID)
            }.toMutableList()
        }

        // 유저의 픽업지 정보를 가져오는 메서드
        suspend fun getUserPickupLocation(userDocumentID: String): String? {
            val userPickLocationInfo = PickupLocationRepository.getUserPickupLocation(userDocumentID)
            return userPickLocationInfo
        }

    }
}