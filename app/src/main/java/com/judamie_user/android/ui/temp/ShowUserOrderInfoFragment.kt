package com.judamie_user.android.ui.temp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentShowUserOrderInfoBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.viewmodel.temp.ShowUserOrderInfoViewModel

class ShowUserOrderInfoFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentShowUserOrderInfoBinding: FragmentShowUserOrderInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShowUserOrderInfoBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_show_user_order_info,container,false)
        fragmentShowUserOrderInfoBinding.showUserOrderInfoViewModel = ShowUserOrderInfoViewModel(this)
        fragmentShowUserOrderInfoBinding.lifecycleOwner = viewLifecycleOwner

        return fragmentShowUserOrderInfoBinding.root
    }


}