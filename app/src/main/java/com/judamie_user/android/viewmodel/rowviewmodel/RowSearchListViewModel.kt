package com.judamie_user.android.viewmodel.rowviewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class RowSearchListViewModel(val fragment: Fragment) : ViewModel() {

    // 상품 이미지
    // imageViewSearchProduct - srcCompact
    val imageViewSearchProductSrcCompact = MutableLiveData("")

    // 상품 이름
    // textViewSearchProductName - text
    val textViewSearchProductNameText = MutableLiveData("")

    // 상품 할인율
    // textViewSearchDiscountRating - text
    val textViewSearchDiscountRatingText = MutableLiveData("")

    // 상품 가격 (할인된 가격)
    // textViewSearchProductPrice - text
    val textViewSearchProductPriceText = MutableLiveData("")

    // 리뷰 (리뷰 (98))
    // textViewSearchProductReview - text
    val textViewSearchProductReviewText = MutableLiveData("")

    // 판매자
    // textViewSearchProductSeller - text
    val textViewSearchProductSellerText = MutableLiveData("")

    // 찜 여부 상태
    // imageButtonSearchSetWishList - srcCompact
    val imageButtonSearchSetWishListSrcCompact = MutableLiveData(false)

    // 찜 목록 추가/제거 버튼 동작
    // imageButtonSearchSetWishList - onClick
    fun imageButtonSearchSetWishListOnClick() {

    }
}