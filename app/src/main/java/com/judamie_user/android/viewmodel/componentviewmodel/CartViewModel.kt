package com.judamie_user.android.viewmodel.componentviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.component.CartDialogFragment

class CartViewModel(val cartDialogFragment: CartDialogFragment) : ViewModel(){

    //cartTitle - text
    val cartTitleText = MutableLiveData("")

    val cartDetailText = MutableLiveData("")

    fun closeOnClick(){
        cartDialogFragment.buttonClose()
    }
}