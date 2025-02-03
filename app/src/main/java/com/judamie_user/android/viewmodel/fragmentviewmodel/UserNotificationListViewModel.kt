package com.judamie_user.android.viewmodel.fragmentviewmodel

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.subfragment.UserNotificationListFragment


class UserNotificationListViewModel(val userNotificationListFragment: UserNotificationListFragment):ViewModel() {
    // materialToolbarUserNotificationList
    // materialToolbarUserNotificationList
    companion object {
        // materialToolbarUserNotificationList - onNavigationClickBarUserNotificationList
        @JvmStatic
        @BindingAdapter("onNavigationClickBarUserNotificationList")
        fun onNavigationClickBarUserNotificationList(
            materialToolbar: MaterialToolbar,
            userNotificationListFragment: UserNotificationListFragment
        ) {
            materialToolbar.setNavigationOnClickListener {
                userNotificationListFragment.movePrevFragment()
            }
        }
    }

    // TextView visibility
    val textViewNoNotificationVisibleVisibility = MutableLiveData(View.GONE)

    //progressbar visibility
    val progressBarUserNotificationVisibleVisibility = MutableLiveData(View.VISIBLE)

    //recyclerViewUserNotificationList visibility
    val recyclerViewUserNotificationListVisibleVisibility = MutableLiveData(View.GONE)

    // visibility 변경
    fun setNoNotificationVisibility(visible: Boolean) {
        textViewNoNotificationVisibleVisibility.value = if (visible) View.VISIBLE else View.GONE
    }

    fun setProgressBarUserNotificationVisibleVisibility(visible: Boolean){
        progressBarUserNotificationVisibleVisibility.value = if (visible) View.VISIBLE else View.GONE
    }

    fun setRecyclerViewUserNotificationListVisibleVisibility(visible: Boolean){
        recyclerViewUserNotificationListVisibleVisibility.value = if (visible)View.VISIBLE else View.GONE
    }
}