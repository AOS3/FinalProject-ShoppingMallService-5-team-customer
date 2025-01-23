package com.judamie_user.android.viewmodel.rowviewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.fragment.ShopCartFragment

data class RowCartProductListViewModel(val shopCartFragment: ShopCartFragment) :ViewModel() {
    //imageViewCartProduct - 상품 이미지

    //progressBarProductImage - 이미지로딩바

    //textViewCartProductName - Text
    val textViewCartProductNameText = MutableLiveData("")

    //textViewCartProductPercent - Text
    val textViewCartProductPercentText = MutableLiveData("")

    //textViewCartProductPrice - Text
    val textViewCartProductPriceText = MutableLiveData("")

    // textViewCartProductCnt - Text
    val textViewCartProductCntText = MutableLiveData("")

    // 상품 선택 체크박스
    // checkBoxCartProduct - checked
    val checkBoxCartProductChecked = MutableLiveData(false)

    // 상품 이미지
    // imageViewCartProduct - ImageUrl
    val imageViewCartProductImageUrl : MutableLiveData<Uri> = MutableLiveData()

    // 수량 버튼
    // buttonCartProductCntMinus - OnClick
    fun buttonCartProductCntMinusOnClick(){
        shopCartFragment.selectProductCount(false)
    }

    // 수량 버튼
    // buttonCartProductCntPlus - OnClick
    fun buttonCartProductCntPlusOnClick(){
        shopCartFragment.selectProductCount(true)
    }


}