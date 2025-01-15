package com.judamie_user.android.viewmodel.rowviewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.WishListFragment

class RowProductListViewModel(val wishListFragment: WishListFragment): ViewModel(){

    //imageViewProductImage 이미지뷰

    //progressBarProductImage 이미지로딩바

    //textViewSoldOut 품절표시

    //textViewProductName 상품명
    val textViewProductNameText = MutableLiveData("상품명")

    //textViewDiscountRating 할인률
    val textViewDiscountRatingText = MutableLiveData("할인률")

    //textViewProductPrice 상품가격
    val textViewProductPriceText = MutableLiveData("상품가격")

    // textViewProductReview
    val textViewProductReviewText = MutableLiveData("리뷰")

    //textViewProductReviewCount 리뷰갯수
    val textViewProductReviewCountText = MutableLiveData("(n)")

    //imageButtonSetWishList 이미지버튼(찜추가/삭제)
    fun imageViewSetWishListOnCLick(){

    }

    //textViewProductSeller 상품판매자
    val textViewProductSellerText = MutableLiveData("상품판매자")

}