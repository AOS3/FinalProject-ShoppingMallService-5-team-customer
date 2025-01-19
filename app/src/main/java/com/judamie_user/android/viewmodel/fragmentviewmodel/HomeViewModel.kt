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
    val buttonHomePickupLocText = MutableLiveData("03900, 서울 마포구 하늘공원로 84 (상암동)")
}