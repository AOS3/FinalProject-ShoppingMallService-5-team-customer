package com.judamie_user.android.ui.temp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentUserNotificationListBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.viewmodel.temp.UserNotificationListViewModel


class UserNotificationListFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentUserNotificationListBinding: FragmentUserNotificationListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentUserNotificationListBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_notification_list,container,false)
        fragmentUserNotificationListBinding.userNotificationListViewModel = UserNotificationListViewModel(this)
        fragmentUserNotificationListBinding.lifecycleOwner = viewLifecycleOwner

        return fragmentUserNotificationListBinding.root
    }


}