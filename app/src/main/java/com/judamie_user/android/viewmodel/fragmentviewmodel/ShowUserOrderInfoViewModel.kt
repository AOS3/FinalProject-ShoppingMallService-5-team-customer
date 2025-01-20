package com.judamie_user.android.viewmodel.fragmentviewmodel

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
    val textViewShowUserOrderInfoDateText = MutableLiveData("textViewShowUserOrderInfoDate")

    //textViewShowUserOrderInfoPickupLocation
    val textViewShowUserOrderInfoPickupLocationText = MutableLiveData("textViewShowUserOrderInfoPickupLocation")

    //buttonShowUserOrderInfoPickupDone
    fun buttonShowUserOrderInfoPickupDoneOnclick(){

    }

}