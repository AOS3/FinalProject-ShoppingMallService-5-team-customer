package com.judamie_user.android.firebase.service

import com.judamie_manager.firebase.model.CouponModel
import com.judamie_manager.firebase.vo.CouponVO
import com.judamie_user.android.firebase.model.ProductModel
import com.judamie_user.android.firebase.repository.CouponRepository
import com.judamie_user.android.firebase.repository.ProductRepository
import com.judamie_user.android.firebase.repository.UserRepository

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

        // userCoupons에 포함된 쿠폰 ID를 기반으로 상품 정보를 가져오기
        suspend fun gettingCouponList(userCouponList: List<String>) : List<CouponModel>{
            return CouponRepository.gettingCouponList(userCouponList)
        }

    }
}