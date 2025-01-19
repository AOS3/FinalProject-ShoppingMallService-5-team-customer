package com.judamie_user.android.viewmodel.componentviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShowPickupLocationInformationDialogViewModel:ViewModel() {
    //textViewDialogShowPickupLocationInformationName - text
    val textViewDialogShowPickupLocationInformationNameText = MutableLiveData("")

    //buttonPickupLocationCall -onclick
    var onCallClick: (()->Unit)? = null
    fun buttonPickupLocationCallOnClick(){
        onCallClick?.invoke()
    }

    //textViewDialogShowPickupLocationInformationOpenTime - text
    val textViewDialogShowPickupLocationInformationOpenTimeText = MutableLiveData("")

    //textViewDialogShowPickupLocationInformationStreetAddress - text
    val textViewDialogShowPickupLocationInformationStreetAddressText = MutableLiveData("")

    //textViewDialogShowPickupLocationInformationAddressDetail - text
    val textViewDialogShowPickupLocationInformationAddressDetailText = MutableLiveData("")

    //textViewDialogShowPickupLocationInformationPhoneNumber - text
    val textViewDialogShowPickupLocationInformationPhoneNumberText = MutableLiveData("")

    //textViewDialogShowPickupLocationInformationDetail - text
    val textViewDialogShowPickupLocationInformationDetailText = MutableLiveData("")

    //DialogShowPickupLocationInformationClose -onclick
    var onCloseClick: (()->Unit)? = null
    fun DialogShowPickupLocationInformationCloseOnClick(){
        onCloseClick?.invoke()
    }

}