package com.judamie_user.android.ui.temp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentShowAppInfoBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.viewmodel.temp.ShowAppInfoViewModel

class ShowAppInfoFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentShowAppInfoBinding: FragmentShowAppInfoBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShowAppInfoBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_show_app_info,container,false)
        fragmentShowAppInfoBinding.showAppInfoViewModel = ShowAppInfoViewModel(this)
        fragmentShowAppInfoBinding.lifecycleOwner = viewLifecycleOwner

        return fragmentShowAppInfoBinding.root
    }


}