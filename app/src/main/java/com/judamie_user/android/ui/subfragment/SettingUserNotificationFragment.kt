package com.judamie_user.android.ui.subfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentSettingUserNotificationBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.viewmodel.fragmentviewmodel.SettingUserNotificationViewModel

class SettingUserNotificationFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentSettingUserNotificationBinding: FragmentSettingUserNotificationBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSettingUserNotificationBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_setting_user_notification,container,false)
        fragmentSettingUserNotificationBinding.settingUserNotificationViewModel = SettingUserNotificationViewModel(this)
        fragmentSettingUserNotificationBinding.lifecycleOwner = this

        return fragmentSettingUserNotificationBinding.root
    }

    fun movePrevFragment() {
        mainFragment.removeFragment(ShopSubFragmentName.SETTING_USER_NOTIFICATION_FRAGMENT)
    }


}