package com.judamie_user.android.viewmodel.componentviewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShowPickupLocationDialogViewModel : ViewModel() {

    // pickupLocationName - text
    val pickupLocationNameText = MutableLiveData("")

    // pickupLocationDetail - text
    val pickupLocationDetailText = MutableLiveData("")

    // pickupLocationDetail - visibility
    val pickupLocationDetailTextVisibility = MutableLiveData(View.VISIBLE)


    // Call button onClick
    var onCallClick: (() -> Unit)? = null
    fun buttonPickupLocationCallOnClick() {
        onCallClick?.invoke()
    }

    // Cancel button onClick
    var onCancelClick: (() -> Unit)? = null
    fun buttonPickupLocationCancleOnClick() {
        onCancelClick?.invoke()
    }

    // Select button onClick
    var onSelectClick: (() -> Unit)? = null
    fun buttonPickupLocationSelectOnClick() {
        onSelectClick?.invoke()
    }
}
