package com.judamie_user.android.firebase.service

import com.judamie_manager.firebase.model.CouponModel
import com.judamie_manager.firebase.vo.CouponVO
import com.judamie_user.android.firebase.repository.CouponRepository

class CouponService {
    companion object{
        // 모든 쿠폰 데이터를 가져오는 메서드
        suspend fun gettingAllCoupons(): MutableList<CouponModel> {
            val couponVOList: MutableList<CouponVO> = CouponRepository.gettingCouponList()
            return couponVOList.map { couponVO ->
                val couponDocumentID = couponVO.couponName
                couponVO.toCouponModel(couponDocumentID)
            }.toMutableList()
        }

    }
}