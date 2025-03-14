package com.judamie_user.android.viewmodel.rowviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.subfragment.ShowUserCouponListFragment

class RowCouponViewModel(val showUserCouponListFragment: ShowUserCouponListFragment):ViewModel() {

    //textViewRowCouponName
    val textViewRowCouponNameText = MutableLiveData("")

    //textViewRowCouponDiscountRate
    val textViewRowCouponDiscountRateText = MutableLiveData("")

    //textViewRowCouponExpireDate
    val textViewRowCouponExpireDateText = MutableLiveData("")

    //textViewRowCouponState
    val textViewRowCouponStateText = MutableLiveData("")



}