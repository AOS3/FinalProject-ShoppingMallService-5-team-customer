package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.R
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


    // textViewProductInfoReviewCnt - onClick
    fun textViewProductInfoReviewCntOnClick() {
        productInfoFragment.moveToReviewList()
    }


    // 찜 버튼 아이콘 상태 (LiveData 사용)
    val imageButtonProductInfoSetWishListSrcCompat =
        MutableLiveData(R.drawable.bookmark_filled_24px)

    // 찜 상태를 저장하는 변수
    private var isWishListed = false

    // 찜 버튼 클릭 이벤트
    fun imageButtonProductInfoSetWishListOnClick() {
        isWishListed = !isWishListed  // 상태 변경

        // UI를 즉시 업데이트 (setValue() 사용)
        imageButtonProductInfoSetWishListSrcCompat.value =
            if (isWishListed) R.drawable.bookmark_filled_24px else R.drawable.bookmark_outline_24px

        // Firebase에 데이터 업데이트
        productInfoFragment.updateWishListInFirebase(isWishListed)
    }

    // 초기 찜 상태 설정 (Firebase에서 가져온 값으로 초기화)
    fun setWishListStatus(isWishListed: Boolean) {
        this.isWishListed = isWishListed
        imageButtonProductInfoSetWishListSrcCompat.value =
            if (isWishListed) R.drawable.bookmark_filled_24px else R.drawable.bookmark_outline_24px
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


    companion object {
        // toolbarProductInfo - onNavigationClickProductInfo
        @JvmStatic
        @BindingAdapter("onNavigationClickProductInfo")
        fun onNavigationClickProductInfo(
            materialToolbar: MaterialToolbar,
            productInfoFragment: ProductInfoFragment
        ) {
            materialToolbar.setNavigationOnClickListener {
                // 이전으로 돌아간다.
                productInfoFragment.movePrevFragment()
            }
        }
    }

}