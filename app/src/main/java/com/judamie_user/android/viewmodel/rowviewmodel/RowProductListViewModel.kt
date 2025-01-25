package com.judamie_user.android.viewmodel.rowviewmodel

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.WishListFragment
import java.text.DecimalFormat

class RowProductListViewModel(val wishListFragment: WishListFragment): ViewModel(){

    // 상품 이름
    // textViewSearchProductName - text
    val textViewSearchProductNameText = MutableLiveData("")

    // 상품 할인율
    // textViewSearchDiscountRating - text
    val textViewSearchDiscountRatingText = MutableLiveData("")

    // 상품 할인율 표시
    // textViewSearchDiscountRating - visibility
    var textViewSearchDiscountRatingVisibility = View.VISIBLE

    // 상품 가격 (할인된 가격)
    // textViewSearchProductPrice - text
    val textViewSearchProductPriceText = MutableLiveData("")

    // 할인된 가격을 계산하는 메서드
    fun calculateProductPrice(discountRate:Int,price:Int):String{
        //할인된 가격계산
        var result = (price - (price*(discountRate.toFloat()/100)))
        Log.d("test",result.toString())
        // 가격 포맷변경
        val decimal = DecimalFormat("#,###")
        var finalPrice = decimal.format(result)
        Log.d("test",finalPrice)
        return finalPrice
    }

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