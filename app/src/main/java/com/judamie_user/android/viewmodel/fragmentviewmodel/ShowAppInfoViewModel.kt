package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.subfragment.ShowAppInfoFragment

class ShowAppInfoViewModel(val showAppInfoFragment: ShowAppInfoFragment):ViewModel() {
    companion object {
        //materialToolbarShowAppInfo - onNavigationClickShowAppInfo
        @JvmStatic
        @BindingAdapter("onNavigationClickShowAppInfo")
        fun onNavigationClickShowAppInfo(
            materialToolbar: MaterialToolbar,
            showAppInfoFragment: ShowAppInfoFragment
        ) {
            materialToolbar.setNavigationOnClickListener {
                showAppInfoFragment.movePrevFragment()
            }
        }
    }
}