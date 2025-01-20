package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.subfragment.SetPickUpLocationFragment

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


//

}