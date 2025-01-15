package com.judamie_user.android.viewmodel.rowviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.temp.ShowUserOrderInfoFragment

class RowOrderInfoViewModel(showUserOrderInfoFragment: ShowUserOrderInfoFragment):ViewModel() {
    //textViewRowOrderInfoProductName
    val textViewRowOrderInfoProductNameText = MutableLiveData("textViewRowOrderInfoProductName")

    //textViewRowOrderInfoProductPrice
    val textViewRowOrderInfoProductPriceText = MutableLiveData("textViewRowOrderInfoProductPrice")

    //textViewRowOrderInfoProductCount
    val textViewRowOrderInfoProductCountText = MutableLiveData("textViewRowOrderInfoProductCount")

    //textViewRowOrderInfoProductSeller
    val textViewRowOrderInfoProductSellerText = MutableLiveData("textViewRowOrderInfoProductSeller")

    //textViewRowOrderInfoProductState
    val textViewRowOrderInfoProductState = MutableLiveData("textViewRowOrderInfoProductState")

    //buttonRowOrderInfoWriteReview
    fun buttonRowOrderInfoWriteReviewOnClick(){

    }
}