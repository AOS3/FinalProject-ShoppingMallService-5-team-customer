package com.judamie_user.android.firebase.service

import com.judamie_user.android.firebase.model.OrderPackageModel
import com.judamie_user.android.firebase.repository.OrderPackageRepository

class OrderPackageService {

    companion object {
        // 주문 패키지 추가
        suspend fun addOrderPackageData(orderPackageModel: OrderPackageModel){
            // 데이터를 VO에 담아준다.
            val orderPackageVO = orderPackageModel.toOrderPackageVO()
            // 저장하는 메서드를 호출
            OrderPackageRepository.addOrderPackageData(orderPackageVO, orderPackageModel.orderPackageDocumentId)
        }
    }
}