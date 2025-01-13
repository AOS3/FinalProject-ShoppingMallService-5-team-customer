package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.subfragment.SettingUserNotificationFragment

class SettingUserNotificationViewModel(val settingUserNotificationFragment: SettingUserNotificationFragment):ViewModel() {

    //materialToolbarSettingUserNotification
    companion object{
        //materialToolbarSettingUserNotification - onNavigationClickSettingUserNotification
        @JvmStatic
        @BindingAdapter("onNavigationClickSettingUserNotification")
        fun onNavigationClickSettingUserNotification(
            materialToolbar: MaterialToolbar,
            settingUserNotificationFragment: SettingUserNotificationFragment
        ) {
            materialToolbar.setNavigationOnClickListener {
                settingUserNotificationFragment.movePrevFragment()
            }
        }
    }

    //switchNotificationOnOff

}