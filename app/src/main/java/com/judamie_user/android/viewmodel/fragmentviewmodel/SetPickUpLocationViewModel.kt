package com.judamie_user.android.viewmodel.fragmentviewmodel

import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.firebase.model.PickupLocationModel
import com.judamie_user.android.ui.subfragment.SetPickUpLocationFragment

class SetPickUpLocationViewModel(val setPickUpLocationFragment: SetPickUpLocationFragment) :
    ViewModel() {
    // materialToolbarSetPicupLocation
    companion object {
        @JvmStatic
        @BindingAdapter("onNavigationClickBarSetPickupLocation")
        fun onNavigationClickBarSetPickupLocation(
            materialToolbar: MaterialToolbar,
            setPickUpLocationFragment: SetPickUpLocationFragment
        ) {
            materialToolbar.setNavigationOnClickListener {
                // 이전으로 돌아간다.
                setPickUpLocationFragment.movePrevFragment()
            }
        }
    }

    // 현재 선택된 PickupLocationModel
    val selectedPickupLocation = MutableLiveData<PickupLocationModel?>()

    // textViewSetPickUpLocationName
    val textViewSetPickUpLocationNameText = MutableLiveData("")

    // textViewSetPickUpLocationCenterAddress
    val textViewSetPickUpLocationCenterAddressText = MutableLiveData("")

    // floatingActionButtonSetPickupLocation
    fun floatingActionButtonSetPickupLocationOnClick() {
        setPickUpLocationFragment.getMyLocation()
    }

    // buttonSetPickUpLocationShowInfo
    fun buttonSetPickUpLocationShowInfoOnClick() {
        val pickupLocation = selectedPickupLocation.value
        if (pickupLocation != null) {
            setPickUpLocationFragment.pickUpLocationShowInfo(pickupLocation)
        } else {
            Toast.makeText(
                setPickUpLocationFragment.requireContext(),
                "선택된 픽업 위치가 없습니다. 위치를 선택하세요.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}


