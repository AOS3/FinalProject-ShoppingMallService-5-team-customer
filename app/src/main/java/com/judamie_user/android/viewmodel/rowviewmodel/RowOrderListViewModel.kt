package com.judamie_user.android.viewmodel.rowviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.subfragment.ShowUserOrderListFragment

class RowOrderListViewModel(val showUserOrderListFragment: ShowUserOrderListFragment):ViewModel() {
    // textViewOrderDate
    val textViewOrderDateText = MutableLiveData("textViewOrderDate")

    //imageViewOrderImage


    //textViewOrderName
    val textViewOrderNameText = MutableLiveData("textViewOrderName")

    //textViewCountMore
    val textViewCountMoreText = MutableLiveData("textViewCountMore")

    //textViewTotalPrice
    val textViewTotalPrice = MutableLiveData("textViewTotalPrice")

    //textViewOrderCount
    val textViewOrderCount = MutableLiveData("textViewOrderCount")

    //textViewPickupState
    val textViewPickupState = MutableLiveData("textViewPickupState")
}