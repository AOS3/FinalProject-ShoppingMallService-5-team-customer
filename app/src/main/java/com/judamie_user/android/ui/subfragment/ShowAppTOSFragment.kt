package com.judamie_user.android.ui.subfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentShowAppTOSBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.viewmodel.temp.ShowAppTOSViewModel


class ShowAppTOSFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentShowAppTOSBinding: FragmentShowAppTOSBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShowAppTOSBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_show_app_t_o_s,container,false)
        fragmentShowAppTOSBinding.showAppTOSViewModel = ShowAppTOSViewModel(this)
        fragmentShowAppTOSBinding.lifecycleOwner = viewLifecycleOwner

        return fragmentShowAppTOSBinding.root
    }


}