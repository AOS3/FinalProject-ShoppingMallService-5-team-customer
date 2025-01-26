package com.judamie_user.android.viewmodel.rowviewmodel

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.activity.ShopActivity
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

     // 수량 버튼
    // buttonCartProductCntMinus - OnClick
    fun buttonCartProductCntMinusOnClick(){
        // shopCartFragment.selectProductCount(false)
         //selectProductCount(false)
    }

    // 수량 버튼
    // buttonCartProductCntPlus - OnClick
    fun buttonCartProductCntPlusOnClick(){
        // shopCartFragment.selectProductCount(true)
        //selectProductCount(true)
    }

    // 수량을 업데이트하는 메서드 추가
    fun updateProductCount(newCount: Int) {
        // 수량을 변경하고 LiveData를 업데이트
        textViewCartProductCntText.value = newCount.toString()
        // ShopCartFragment에서 수량을 반영하기
        val position = shopCartFragment.recyclerViewList.indexOfFirst { it.productName == textViewCartProductNameText.value }
        // position이 유효한 경우
        if (position >= 0 && position < shopCartFragment.productCountList.value?.size ?: 0) {
            // 해당 위치의 수량을 productCountList에 반영
            val currentList = shopCartFragment.productCountList.value?.toMutableList() ?: mutableListOf()
            currentList[position] = newCount
            shopCartFragment.productCountList.value = currentList
        } else {
            // position이 유효하지 않으면 에러 로그 출력
            Log.e("ShopCartFragment", "Invalid position: $position for product ${textViewCartProductNameText.value}")
        }
    }

//    // 상품 수량 선택 메서드
//    fun selectProductCount(isIncrease:Boolean) {
//
//        val currentCount = textViewCartProductCntText.value?.toIntOrNull() ?: 1
//
//        if (isIncrease) {
//            // 수량 증가
//             textViewCartProductCntText.value = (currentCount + 1).toString()
//        } else {
//            // 수량 감소 (1 이하로는 감소하지 않음)
//            if (currentCount > 1) {
//                textViewCartProductCntText.value = (currentCount - 1).toString()
//            } else {
//                shopCartFragment.requireActivity().runOnUiThread {
//                    Toast.makeText(
//                        shopCartFragment.requireContext(),
//                        "최소 수량입니다.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        }
//    }


//    fun buttonCartProductCntMinusOnClick(position: Int) {
//        shopCartFragment.selectProductCount(false, position)
//    }
//
//    fun buttonCartProductCntPlusOnClick(position: Int) {
//        shopCartFragment.selectProductCount(true, position)
//    }


}