package com.judamie_user.android.viewmodel.temp

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.temp.SetPickUpLocationFragment
import com.judamie_user.android.ui.temp.ShowUserCouponListFragment

class SetPickUpLocationViewModel(val setPickUpLocationFragment: SetPickUpLocationFragment):ViewModel() {
    //materialToolbarSetPickupLocation
    companion object{
        // materialToolbarSetPicupLocation - onNavigationClickBarSetPickupLocation
        @JvmStatic
        @BindingAdapter("onNavigationClickBarSetPickupLocation")
        fun onNavigationClickBarSetPickupLocation(materialToolbar: MaterialToolbar, setPickUpLocationFragment: SetPickUpLocationFragment){
            materialToolbar.setNavigationOnClickListener {
                // 이전으로 돌아간다.
                setPickUpLocationFragment.movePrevFragment()
            }
        }
    }


    //textViewSetPickUpLocationName
    val textViewSetPickUpLocationNameText = MutableLiveData("")

    //textViewSetPickUpLocationCenterAddress
    val textViewSetPickUpLocationCenterAddressText = MutableLiveData("")

    //floatingActionButtonSetPickupLocation
    fun floatingActionButtonSetPickupLocationOnClick(){
        setPickUpLocationFragment.getMyLocation()
    }

    //buttonSetPickUpLocationShowInfo
    fun buttonSetPickUpLocationShowInfoOnClick(){
        setPickUpLocationFragment.pickUpLocationShowInfo()
    }

}