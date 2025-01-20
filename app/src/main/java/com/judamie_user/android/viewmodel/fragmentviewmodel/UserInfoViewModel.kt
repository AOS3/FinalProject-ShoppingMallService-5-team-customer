package com.judamie_user.android.viewmodel.fragmentviewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.fragment.UserInfoFragment

class UserInfoViewModel(val userInfoFragment: UserInfoFragment) : ViewModel() {
    //textViewUserName
    val textViewUserNameText = MutableLiveData("___ 님")

    //imageButtonGoSetting
    fun imageButtonGoSettingOnClick() {
        userInfoFragment.goUserSettingFragment()
    }

    //textViewOrderList
    fun textViewOrderListOcClick(){
        userInfoFragment.goShowUserOrderListFragment()
    }

    //textViewCouponList
    fun textViewCouponListOnClick(){
        userInfoFragment.goShowUserCouponListFragment()
    }

    //textViewIntroduceAndHowToUse
    fun textViewIntroduceAndHowToUseOnClick(){
        userInfoFragment.goShowAppInfoFragment()
    }

    //textViewAppPrivacyPolicy
    fun textViewAppPrivacyPolicyOnClick(){
        userInfoFragment.goShowAppPrivacyPolicyFragment()
    }

    //textViewAppTOS
    fun textViewAppTOSOnClick(){
        userInfoFragment.goShowAppTOSFragment()
    }
}