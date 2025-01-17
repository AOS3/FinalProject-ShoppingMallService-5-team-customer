package com.judamie_user.android.viewmodel.rowviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.temp.ShowUserCouponListFragment

class RowCouponViewModel(val showUserCouponListFragment: ShowUserCouponListFragment):ViewModel() {

    //textViewRowCouponName
    val textViewRowCouponNameText = MutableLiveData("textViewRowCouponName")

    //textViewRowCouponDiscountRate
    val textViewRowCouponDiscountRateText = MutableLiveData("textViewRowCouponDiscountRate%")

    //textViewRowCouponExpireDate
    val textViewRowCouponExpireDateText = MutableLiveData("textViewRowCouponExpireDate")

    //textViewRowCouponState
    val textViewRowCouponStateText = MutableLiveData("textViewRowCouponState")



}