package com.judamie_user.android.viewmodel.fragmentviewmodel

import android.graphics.drawable.Drawable
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.fragment.ShopCartFragment
import com.judamie_user.android.ui.subfragment.SettingUserNotificationFragment
import com.judamie_user.android.ui.subfragment.ShowUserOrderInfoFragment

class ShowUserOrderInfoViewModel(val showUserOrderInfoFragment: ShowUserOrderInfoFragment):ViewModel() {
    //materialToolbarShowUserOrderInfo
    companion object{
        // materialToolbarShowUserOrderInfo - onNavigationClickMaterialToolbarShowUserOrderInfo
        @JvmStatic
        @BindingAdapter("onNavigationClickMaterialToolbarShowUserOrderInfo")
        fun onNavigationClickMaterialToolbarShowUserOrderInfo(materialToolbar: MaterialToolbar, showUserOrderInfoFragment: ShowUserOrderInfoFragment){
            materialToolbar.setNavigationOnClickListener {
                // 이전으로 돌아간다.
                showUserOrderInfoFragment.movePrevFragment()
            }
        }
    }


    //textViewShowUserOrderInfoDate
    val textViewShowUserOrderInfoDateText = MutableLiveData("")

    //textViewShowUserOrderInfoPickupLocationStreetAddress
    val textViewShowUserOrderInfoPickupLocationStreetAddressText = MutableLiveData("")

    //textViewShowUserOrderInfoPickupLocationAddressDetail
    val textViewShowUserOrderInfoPickupLocationAddressDetailText = MutableLiveData("")

    //buttonShowUserOrderInfoPickupDone
    fun buttonShowUserOrderInfoPickupDoneOnclick(){
        showUserOrderInfoFragment.settingButtonShowUserOrderInfoPickupDone()
    }

    //버튼상태
    // buttonShowUserOrderInfoPickupDone background
    val buttonShowUserOrderInfoPickupDoneBackground = MutableLiveData<Drawable>()

    //버튼텍스트 색상
    // buttonShowUserOrderInfoPickupDone textColor
    val buttonShowUserOrderInfoPickupDoneTextColor = MutableLiveData<Int>()

    //버튼 활성화 상태
    // buttonShowUserOrderInfoPickupDone enabled
    val buttonShowUserOrderInfoPickupDoneEnabled = MutableLiveData<Boolean>()

}