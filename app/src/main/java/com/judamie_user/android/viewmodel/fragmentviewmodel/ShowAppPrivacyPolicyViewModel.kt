package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.subfragment.ShowAppPrivacyPolicyFragment

class ShowAppPrivacyPolicyViewModel(val showAppPrivacyPolicyFragment: ShowAppPrivacyPolicyFragment):ViewModel() {
    companion object {
        //materialToolbarShowAppPrivacyPolicy - onNavigationClickShowAppPrivacyPolicy
        @JvmStatic
        @BindingAdapter("onNavigationClickShowAppPrivacyPolicy")
        fun onNavigationClickShowAppPrivacyPolicy(
            materialToolbar: MaterialToolbar,
            showAppPrivacyPolicyFragment: ShowAppPrivacyPolicyFragment
        ) {
            materialToolbar.setNavigationOnClickListener {
                showAppPrivacyPolicyFragment.movePrevFragment()
            }
        }
    }

}