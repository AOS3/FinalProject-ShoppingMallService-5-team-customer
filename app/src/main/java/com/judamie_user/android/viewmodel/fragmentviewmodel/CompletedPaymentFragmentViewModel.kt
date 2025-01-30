package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.subfragment.CompletedPaymentFragment

data class CompletedPaymentFragmentViewModel(val completedPaymentFragment: CompletedPaymentFragment) : ViewModel() {

    // buttonCompletePaymentCheck - onClick
    fun buttonCompletePaymentCheckOnClick() {
        // 주문 내역 목록 이동 메서드 호출
        completedPaymentFragment.moveToUserOrder()
    }

}