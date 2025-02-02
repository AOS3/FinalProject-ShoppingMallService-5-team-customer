package com.judamie_user.android.viewmodel.rowviewmodel

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.subfragment.ShowUserOrderInfoFragment

class RowOrderInfoViewModel(showUserOrderInfoFragment: ShowUserOrderInfoFragment):ViewModel() {
    //textViewRowOrderInfoProductName
    val textViewRowOrderInfoProductNameText = MutableLiveData("")

    //textViewRowOrderInfoProductPrice
    val textViewRowOrderInfoProductPriceText = MutableLiveData("")

    //textViewRowOrderInfoProductCount
    val textViewRowOrderInfoProductCountText = MutableLiveData("")

    //textViewRowOrderInfoProductSeller
    val textViewRowOrderInfoProductSellerText = MutableLiveData("")

    //textViewRowOrderInfoProductState
    val textViewRowOrderInfoProductState = MutableLiveData("")

    //imageViewRowOrderInfoProductImage - uri
    val imageViewRowOrderInfoProductImageUri = MutableLiveData<Uri>()

    //버튼상태
    // buttonRowOrderInfoWriteReview background
    val buttonRowOrderInfoWriteReviewBackground = MutableLiveData<Drawable>()

    //버튼텍스트 색상
    // buttonRowOrderInfoWriteReview textColor
    val buttonRowOrderInfoWriteReviewTextColor = MutableLiveData<Int>()

    //버튼 활성화 상태
    // buttonRowOrderInfoWriteReview enabled
    val buttonRowOrderInfoWriteReviewEnabled = MutableLiveData<Boolean>()

    //buttonRowOrderInfoWriteReview
    fun buttonRowOrderInfoWriteReviewOnClick(){

    }
}