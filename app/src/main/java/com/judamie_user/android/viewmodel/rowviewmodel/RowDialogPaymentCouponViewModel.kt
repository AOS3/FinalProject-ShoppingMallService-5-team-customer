package com.judamie_user.android.viewmodel.rowviewmodel

import androidx.lifecycle.MutableLiveData
import com.judamie_user.android.ui.subfragment.PaymentProductFragment

data class RowDialogPaymentCouponViewModel(val paymentProductFragment: PaymentProductFragment) {
    // rowRadioButtonDialogCouponItem - checked
    val rowRadioButtonDialogCouponItemChecked = MutableLiveData(false)

    // rowTextViewDialogCouponItem - Text
    val rowTextViewDialogCouponItemText = MutableLiveData("")
}