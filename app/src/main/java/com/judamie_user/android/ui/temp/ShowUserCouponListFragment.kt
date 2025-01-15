package com.judamie_user.android.ui.temp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentShowUserCouponListBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.viewmodel.temp.ShowUserCouponListViewModel

class ShowUserCouponListFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentShowUserCouponListBinding: FragmentShowUserCouponListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShowUserCouponListBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_show_user_coupon_list,container,false)
        fragmentShowUserCouponListBinding.showUserCouponListViewModel = ShowUserCouponListViewModel(this)
        fragmentShowUserCouponListBinding.lifecycleOwner = viewLifecycleOwner

        return fragmentShowUserCouponListBinding.root
    }

}