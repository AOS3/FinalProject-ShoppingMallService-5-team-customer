package com.judamie_user.android.viewmodel.componentviewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.component.ShowPickupLocationDialogFragment

class ShowPickupLocationDialogViewModel(val showPickupLocationDialogFragment: ShowPickupLocationDialogFragment):ViewModel() {

    // pickupLocationName - text
    val pickupLocationNameText = MutableLiveData("")

    // pickupLocationDetail - text
    val pickupLocationDetailText = MutableLiveData("")

    // pickupLocationDetail - visibility
    val pickupLocationDetailTextVisibility = MutableLiveData(View.VISIBLE)

    // buttonPickupLocationCall - onClick
    fun buttonPickupLocationCallOnClick(){
        showPickupLocationDialogFragment.buttonCalling()
    }

    // buttonPickupLocationCancle - onClick
    fun buttonPickupLocationCancleOnClick(){
        showPickupLocationDialogFragment.buttonClose()
    }

    // buttonPickupLocationSelect
    fun buttonPickupLocationSelectOnClick(){

    }

}