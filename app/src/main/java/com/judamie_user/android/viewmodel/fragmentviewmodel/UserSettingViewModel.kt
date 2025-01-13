package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.subfragment.UserSettingFragment

class UserSettingViewModel(val userSettingFragment: UserSettingFragment) : ViewModel() {

    companion object {
        //materialToolbarUserSetting - onNavigationClickUserSetting
        @JvmStatic
        @BindingAdapter("onNavigationClickUserSetting")
        fun onNavigationClickUserSetting(
            materialToolbar: MaterialToolbar,
            userSettingFragment: UserSettingFragment
        ) {
            materialToolbar.setNavigationOnClickListener {
                userSettingFragment.movePrevFragment()
            }
        }
    }

    //textViewModifyUserInfo
    fun textViewModifyUserInfoOnClick() {
        userSettingFragment.goModifyUserInfoFragment()
    }

    //textViewSettingNotification
    fun textViewSettingNotificationOnClick() {
        userSettingFragment.goSettingUserNotificationFragment()
    }

    //textViewMembershipWithdrawal
    fun textViewMembershipWithdrawalOnClick() {
        userSettingFragment.showDialogMembershipWithdrawal()
    }

    //textViewLogout
    fun textViewLogoutOcClick() {
        userSettingFragment.showDialogLogout()
    }


}