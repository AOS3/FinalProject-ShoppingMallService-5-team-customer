package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.fragment.HomeFragment

data class HomeViewModel(val homeFragment: HomeFragment) : ViewModel() {

    // buttonHomeSearch - onClick
    fun buttonHomeSearchOnClick() {
        homeFragment.moveToSearch()
    }

    // buttonHomePickupLoc - onclick
    fun buttonHomePickupLocOnClick(){
        homeFragment.goSetPickupLocationFragment()
    }

    // buttonHomePickupLoc - text
    val buttonHomePickupLocText = MutableLiveData("")
}