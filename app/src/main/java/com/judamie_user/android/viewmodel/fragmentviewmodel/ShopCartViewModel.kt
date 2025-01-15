package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.checkbox.MaterialCheckBox
import com.judamie_user.android.ui.fragment.ShopCartFragment

data class ShopCartViewModel(val shopCartFragment: ShopCartFragment) : ViewModel() {

    // toolbarShopCart - title
    val toolbarShopCartTitle = MutableLiveData<String>()
    // toolbarShopCart - navigationIcon
    val toolbarShopCartNavigationIcon = MutableLiveData<Int>()

    // 장바구니 비어있을 때 텍스트
    // textViewShopCartEmpty - Text
    val textViewShopCartEmpty =
            "장바구니가 비어있어요.\n" +
            "원하는 상품을 담아보세요."

    // 상품 선택
    // checkBoxCartProductAll - checkedState
    val checkBoxCartProductAllCheckedState = MutableLiveData(MaterialCheckBox.STATE_UNCHECKED)

    // checkBoxCartProductAll - onClick
    fun checkBoxUserJoinStep2HobbyAllOnClick(){

    }

    // 장바구니 비었을 때 버튼
    // buttonShopCartEmpty - OnClick
    fun buttonShopCartEmptyOnClick() {
        // 상품 목록 (홈) 화면 이동 메서드 호출
    }

    // 선택된 장바구니 품목 삭제 버튼
    // buttonShopCartDelete- onClick
    fun buttonShopCartDeleteOnClick(){

    }

    // 결제하기 버튼
    // buttonCartProductSelected - onClick
    fun buttonCartProductSelectedOnClick() {
        shopCartFragment.moveToPaymentProduct()
    }

    companion object{
        // toolbarShopCart - onNavigationClickRegisterStep1
        @JvmStatic
        @BindingAdapter("onNavigationClickShopCart")
        fun onNavigationClickShopCart(materialToolbar: MaterialToolbar, shopCartFragment: ShopCartFragment){
            materialToolbar.setNavigationOnClickListener {
                // 이전으로 돌아간다.
                shopCartFragment.movePrevFragment()
            }
        }
    }
}