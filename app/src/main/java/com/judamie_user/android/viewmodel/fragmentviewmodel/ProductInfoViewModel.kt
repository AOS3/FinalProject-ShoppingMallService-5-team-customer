package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.subfragment.ProductInfoFragment

data class ProductInfoViewModel(val productInfoFragment: ProductInfoFragment) : ViewModel() {

    // toolbarProductInfo - title
    val toolbarProductInfoTitle = MutableLiveData<String>()
    // toolbarProductInfo - navigationIcon
    val toolbarProductInfoNavigationIcon = MutableLiveData<Int>()

    // textViewProductInfoProductCategory - text
    val textViewProductInfoProductCategoryText = MutableLiveData("")
    // textViewProductInfoProductName - text
    val textViewProductInfoProductNameText = MutableLiveData("")
    // textViewProductInfoDiscountPercent - text
    val textViewProductInfoDiscountPercentText = MutableLiveData("")
    // textViewProductInfoPrice - text
    val textViewProductInfoPriceText = MutableLiveData("")
    // textViewProductInfoCnt - text
    val textViewProductInfoCntText = MutableLiveData("1")
    // textViewProductInfoDescription
    val textViewProductInfoDescriptionText = MutableLiveData("")
    // textViewProductInfoReviewCnt
    val textViewProductInfoReviewCnt = MutableLiveData("")

    // 찜 목록 추가 버튼
    // imageButtonProductInfoSetWishList - OnClick
    fun imageButtonProductInfoSetWishListOnClick() {

    }

    // 상품 수량 빼기
    // buttonProductInfoCntMinus - onClick
    fun buttonProductInfoCntMinusOnClick() {
        productInfoFragment.selectProductCount(false)
    }

    // 상품 수량 더하기
    // buttonProductInfoCntPlus - onClick
    fun buttonProductInfoCntPlusOnClick() {
        productInfoFragment.selectProductCount(true)
    }

    // 장바구니 추가
    // buttonProductInfoAddCart - onClick
    fun buttonProductInfoAddCartOnClick() {
        productInfoFragment.addCartProduct()
    }

    // 바로 구매
    // buttonProductInfoBuy - onClick
    fun buttonProductInfoBuyOnClick() {
        productInfoFragment.buyProduct()
    }

    companion object{
        // toolbarProductInfo - onNavigationClickProductInfo
        @JvmStatic
        @BindingAdapter("onNavigationClickProductInfo")
        fun onNavigationClickProductInfo(materialToolbar: MaterialToolbar, productInfoFragment: ProductInfoFragment){
            materialToolbar.setNavigationOnClickListener {
                // 이전으로 돌아간다.
                productInfoFragment.movePrevFragment()
            }
        }
    }

}