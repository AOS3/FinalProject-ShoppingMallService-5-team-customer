package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.fragment.HomeFragment

data class HomeViewModel(val homeFragment: HomeFragment) : ViewModel() {

    // buttonHomeSearch - onClick
    fun buttonHomeSearchOnClick() {
        homeFragment.moveToSearch()
    }

    //buttonHomePickupLoc - onClick
    fun buttonHomePickupLocOnClick(){
        homeFragment.moveToPickLoc()
    }

    //buttonHomePickupLoc - Text
    val buttonHomePickupLocText = MutableLiveData("없음")
}