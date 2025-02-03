package com.judamie_user.android.firebase.service

import com.judamie_user.android.firebase.model.OrderPackageModel
import com.judamie_user.android.firebase.repository.OrderPackageRepository
import com.judamie_user.android.firebase.vo.OrderPackageVO

class OrderPackageService {

    companion object {
        // 주문 패키지 추가
        suspend fun addOrderPackageData(orderPackageModel: OrderPackageModel){
            // 데이터를 VO에 담아준다.
            val orderPackageVO = orderPackageModel.toOrderPackageVO()
            // 저장하는 메서드를 호출
            OrderPackageRepository.addOrderPackageData(orderPackageVO, orderPackageModel.orderPackageDocumentId)
        }
        // 주문 패키지를 가져오는메서드
        suspend fun gettingOrderPackageData(userDocumentID: String): MutableList<OrderPackageModel> {
            val map = OrderPackageRepository.gettingOrderPackageData(userDocumentID)

            val list = mutableListOf<OrderPackageModel>()

            map.forEach { ID, orderPackageVO ->
                val orderPackageModel = orderPackageVO.toOrderPackageModel(ID)
                list.add(orderPackageModel)
            }
            return list
        }
    }
}