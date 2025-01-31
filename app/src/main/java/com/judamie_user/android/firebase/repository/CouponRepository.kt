package com.judamie_user.android.firebase.repository


import android.util.Log
import com.google.firebase.Timestamp
import com.judamie_manager.firebase.vo.CouponVO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.judamie_manager.firebase.model.CouponModel
import com.judamie_user.android.firebase.model.ProductModel
import com.judamie_user.android.firebase.vo.ProductVO
import com.judamie_user.android.util.CouponUsableType
import com.judamie_user.android.util.ProductState
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class CouponRepository {
    companion object{

        // 모든 쿠폰 데이터를 가져오는 메서드
        suspend fun gettingCouponList(): MutableList<CouponVO> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("CouponData")
            val result = collectionReference.get().await()
            return result.toObjects(CouponVO::class.java).toMutableList()
        }

        suspend fun gettingCouponList(couponIds: List<String>): List<CouponModel> {
            val couponList = mutableListOf<CouponModel>()
            val couponRef = FirebaseFirestore.getInstance().collection("CouponData")

            couponIds.forEach { id ->
                val document = couponRef.document(id).get().await()
                if (document.exists()) {
                    // DocumentSnapshot에서 데이터 추출
                    val data = document.data
                    if (data != null) {
                        val couponModel = CouponModel()

                        // Firestore 문서 ID를 productDocumentId로 설정
                        couponModel.couponDocumentID = document.id
                        couponModel.couponName = data["couponName"] as? String ?: ""
                        // 판매자 측에서 DiscountRate/Period Type 변경 후, 작업 진행 예정
                        couponModel.couponDiscountRate = when (val discountData = data["couponDiscountRate"]) {
                            is Long -> discountData.toInt() // Firestore에서 Long으로 저장된 경우
                            is String -> discountData.toIntOrNull() ?: 0 // String으로 저장된 경우 변환
                            else -> 0
                        }
                        couponModel.couponPeriod = when (val periodData = data["couponPeriod"]) {
                            is Timestamp -> periodData.toDate().time // Timestamp를 Long으로 변환
                            is Long -> periodData // 이미 Long이면 그대로 사용
                            is String -> periodData.toLongOrNull() ?: 0L // String으로 저장된 경우 변환
                            else -> 0L
                        }


                        // productState를 Long으로 가져와서 enum으로 변환
                        val couponStateLong = data["couponState"] as? Long ?: 1L
                        couponModel.couponState = CouponUsableType.fromNumber(couponStateLong.toInt())

//                        // 로그로 각 필드 출력 (toString()을 사용하지 않음)
//                        Log.d("test100", "쿠폰: CouponDocumentId=${couponModel.couponDocumentID}, " +
//                                "쿠폰이름=${couponModel.couponName}, 할인률=${couponModel.couponDiscountRate}, " +
//                                "쿠폰상태=${couponModel.couponState}")

                        couponList.add(couponModel)
                    }
                }
            }
            // Log.d("test", couponList[0].couponState.toString()) //1
            return couponList
        }


    }
}