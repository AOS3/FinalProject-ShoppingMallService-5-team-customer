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

    // 상품 선택 체크박스
    // checkBoxCartProductAll - checkedBox
    val checkBoxCartProductAllCheckedState = MutableLiveData(false)


    // checkBoxCartProductAll - onClick
    fun checkBoxCartProductAllOnClick(){
        if (checkBoxCartProductAllCheckedState.value == false){
            val isChecked = true
            shopCartFragment.updateAllCheckBoxes(isChecked)
        }
        else {
            val isChecked = false
            shopCartFragment.updateAllCheckBoxes(isChecked)
        }
    }


    // 선택된 장바구니 품목 삭제 버튼
    // buttonShopCartDelete- onClick
    fun buttonShopCartDeleteOnClick(){
        shopCartFragment.selectionDelete()
    }

    // 결제하기 버튼
    // buttonCartProductSelected - onClick
    fun buttonCartProductSelectedOnClick() {
        shopCartFragment.moveToPaymentProduct()
    }

    // 장바구니 비었을때 상품 보러가기 버튼
    // buttonShopCartEmpty - onClick
    fun buttonShopCartEmptyOnClick() {
        shopCartFragment.moveHomeFragment()
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