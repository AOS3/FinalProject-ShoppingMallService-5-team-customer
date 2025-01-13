package com.judamie_user.android.viewmodel.rowviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.subfragment.ShowUserOrderListFragment

class RowOrderListViewModel(val showUserOrderListFragment: ShowUserOrderListFragment):ViewModel() {
    // textViewOrderDate
    val textViewOrderDateText = MutableLiveData("")

    //imageViewOrderImage


    //textViewOrderName
    val textViewOrderNameText = MutableLiveData("")

    //textViewCountMore
    val textViewCountMoreText = MutableLiveData("")

    //textViewTotalPrice
    val textViewTotalPrice = MutableLiveData("")

    //textViewOrderCount
    val textViewOrderCount = MutableLiveData("")

    //textViewPickupState
    val textViewPickupState = MutableLiveData("")
}