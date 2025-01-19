package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.subfragment.ShowAppTOSFragment

class ShowAppTOSViewModel(val showAppTOSFragment: ShowAppTOSFragment):ViewModel() {
    companion object {
            //materialToolbarShowAppTOS - onNavigationClickShowAppTOS
            @JvmStatic
            @BindingAdapter("onNavigationClickShowAppTOS")
            fun onNavigationClickShowAppTOS(
                materialToolbar: MaterialToolbar,
                showAppTOSFragment: ShowAppTOSFragment
            ) {
                materialToolbar.setNavigationOnClickListener {
                    showAppTOSFragment.movePrevFragment()
                }
            }
        }
}