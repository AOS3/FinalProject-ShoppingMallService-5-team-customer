package com.judamie_user.android.viewmodel.rowviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.subfragment.PaymentProductFragment

data class RowPaymentProductListViewModel(val paymentProductFragment: PaymentProductFragment) : ViewModel() {

    //imageViewCartProduct - 상품 이미지
    val imageViewCartProductSrc = MutableLiveData("")
    //progressBarProductImage - 이미지로딩바

    //textViewCartProductName - Text
    val textViewCartProductNameText = MutableLiveData("")

    //rowTextViewPaymentProductPercent - Text
    val rowTextViewPaymentProductPercentText = MutableLiveData("")

    //rowTextViewPaymentProductPrice - Text
    val rowTextViewPaymentProductPriceText = MutableLiveData("")

    // rowTextViewPaymentProductCount - Text
    val rowTextViewPaymentProductCountText = MutableLiveData("")

    // rowTextViewPaymentStoreName - Text
    val rowTextViewPaymentStoreNameText = MutableLiveData("")
}