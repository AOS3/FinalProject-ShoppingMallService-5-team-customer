package com.judamie_user.android.ui.subfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentShowAppPrivacyPolicyBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.viewmodel.fragmentviewmodel.ShowAppPrivacyPolicyViewModel


class ShowAppPrivacyPolicyFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentShowAppPrivacyPolicyBinding: FragmentShowAppPrivacyPolicyBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShowAppPrivacyPolicyBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_show_app_privacy_policy,container,false)
        fragmentShowAppPrivacyPolicyBinding.showAppPrivacyPolicyViewModel = ShowAppPrivacyPolicyViewModel(this)
        fragmentShowAppPrivacyPolicyBinding.lifecycleOwner = viewLifecycleOwner


        return fragmentShowAppPrivacyPolicyBinding.root
    }


}