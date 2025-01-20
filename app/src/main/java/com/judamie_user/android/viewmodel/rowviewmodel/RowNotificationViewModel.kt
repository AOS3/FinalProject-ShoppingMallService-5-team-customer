package com.judamie_user.android.viewmodel.rowviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.subfragment.UserNotificationListFragment

class RowNotificationViewModel(val userNotificationListFragment: UserNotificationListFragment):ViewModel() {
    //textViewRowNotificationProductName
    val textViewRowNotificationProductNameText = MutableLiveData("textViewRowNotificationProductName")

}