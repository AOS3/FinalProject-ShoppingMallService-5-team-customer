package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.activity.FragmentName
import com.judamie_user.android.ui.fragment.RegisterVerificationFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.ui.subfragment.PaymentProductFragment

data class PaymentProductViewModel(val paymentProductFragment: PaymentProductFragment) : ViewModel() {

    // toolbarPaymentProduct - title
    val toolbarPaymentProductTitle = MutableLiveData<String>()
    // toolbarPaymentProduct - navigationIcon
    val toolbarPaymentProductNavigationIcon = MutableLiveData<Int>()

    // 구매자 정보 이름
    // textViewPaymentUserName - Text
    val textViewPaymentUserNameText = MutableLiveData("")

    // 구매자 정보 핸드폰 번호
    // textViewPaymentUserNo - Text
    val textViewPaymentUserNoText = MutableLiveData("")

    // 총 결제 할 상품 수량
    // textViewPaymentProductCount - Text
    val textViewPaymentProductCountText = MutableLiveData("")

    // 픽업지 정보
    // textViewPaymentPickupLocName - Text
    val textViewPaymentPickupLocNameText = MutableLiveData("")

    // 상품 총 가격
    // textViewPaymentAllProductPrice - Text
    val textViewPaymentAllProductPriceText = MutableLiveData("")

    // 할인 가격
    // textViewPaymentCouponDiscountPrice - Text
    val textViewPaymentCouponDiscountPriceText = MutableLiveData("")

    // 결제할 가격
    // textViewPaymentTotalPrice - Text
    val textViewPaymentTotalPriceText = MutableLiveData("")

    // 쿠폰 선택 버튼
    // buttonPaymentSelectCoupon - text
    val buttonPaymentSelectCouponText = MutableLiveData("")

    // 결제하기 버튼
    // buttonPaymentPay - text
    val buttonPaymentPayText = MutableLiveData("")

    // 쿠폰 선택 버튼
    // buttonPaymentSelectCoupon - onClick
    fun buttonPaymentSelectCouponOnClick() {
        // 쿠폰 선택 다이얼로그 띄우는 메서드 호출
        paymentProductFragment.settingUserCoupons()
    }

    // 결제하기 버튼
    // buttonPaymentPay - onClick
    fun buttonPaymentPayOnClick() {

    }

    companion object{
        // toolbarPaymentProduct - onNavigationClickPaymentProduct
        @JvmStatic
        @BindingAdapter("onNavigationClickPaymentProduct")
        fun onNavigationClickPaymentProduct(materialToolbar: MaterialToolbar, paymentProductFragment: PaymentProductFragment){
            materialToolbar.setNavigationOnClickListener {
                // 이전으로 돌아간다.
                paymentProductFragment.movePrevFragment()
            }
        }

    }


}