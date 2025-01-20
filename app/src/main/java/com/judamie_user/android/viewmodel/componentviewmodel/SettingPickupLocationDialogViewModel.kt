package com.judamie_user.android.viewmodel.componentviewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingPickupLocationDialogViewModel : ViewModel() {

    //textViewDialogSettingPickupLocationName - text
    val textViewDialogSettingPickupLocationNameText = MutableLiveData("")

    //buttonPickupLocationCall -onclick
    var onCallClick: (()->Unit)? = null
    fun buttonPickupLocationCallOnClick(){
        onCallClick?.invoke()
    }

    //textViewDialogSettingPickupLocationOpenTime - text
    val textViewDialogSettingPickupLocationOpenTimeText = MutableLiveData("")

    //textViewDialogSettingPickupLocationStreetAddress - text
    val textViewDialogSettingPickupLocationStreetAddressText = MutableLiveData("")

    //textViewDialogSettingPickupLocationAddressDetail - text
    val textViewDialogSettingPickupLocationAddressDetailText = MutableLiveData("")

    //textViewDialogSettingPickupLocationPhoneNumber - text
    val textViewDialogSettingPickupLocationPhoneNumberText = MutableLiveData("")

    //textViewDialogSettingPickupLocationDetail - text
    val textViewDialogSettingPickupLocationDetailText = MutableLiveData("")

    //DialogSettingPickupLocationClose -onclick
    var onCloseClick: (()->Unit)? = null
    fun DialogSettingPickupLocationCloseOnClick(){
        onCloseClick?.invoke()
    }

    //DialogSettingPickupLocationChoice -onclick
    var onChoiceClick: (()->Unit)? = null
    fun DialogSettingPickupLocationChoiceOnClick(){
        onChoiceClick?.invoke()
    }
}
