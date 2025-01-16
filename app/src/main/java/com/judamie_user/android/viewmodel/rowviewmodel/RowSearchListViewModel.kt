package com.judamie_user.android.viewmodel.rowviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.fragment.SearchFragment

data class RowSearchListViewModel(val searchFragment: SearchFragment) : ViewModel() {

    // 상품 이름
    // textViewSearchProductName - text
    val textViewSearchProductNameText = MutableLiveData("")

    // 상품 할인율
    // textViewSearchDiscountRating - text
    val textViewSearchDiscountRatingText = MutableLiveData("")

    // 상품 가격 (할인된 가격)
    // textViewSearchProductPrice - text
    val textViewSearchProductPriceText = MutableLiveData("")

    // 리뷰 유무 (텍스트)
    // textViewSearchProductReview - text
    val textViewSearchProductReviewText = MutableLiveData("")

    // 리뷰 수
    // textViewSearchProductReviewCount - text
    val textViewSearchProductReviewCountText = MutableLiveData("")

    // 판매자
    // textViewSearchProductSeller - text
    val textViewSearchProductSellerText = MutableLiveData("")

    // 찜 목록 추가/제거 버튼
    // imageButtonSearchSetWishList - onClick
    fun imageButtonSearchSetWishListOnClick() {

    }
}