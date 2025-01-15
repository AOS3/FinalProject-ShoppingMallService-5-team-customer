package com.judamie_user.android.ui.subfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentModifyUserInfoBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.viewmodel.fragmentviewmodel.ModifyUserInfoViewModel

class ModifyUserInfoFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentModifyUserInfoBinding: FragmentModifyUserInfoBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentModifyUserInfoBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_modify_user_info,container,false)
        fragmentModifyUserInfoBinding.modifyUserInfoViewModel = ModifyUserInfoViewModel(this)
        fragmentModifyUserInfoBinding.lifecycleOwner = this

        return fragmentModifyUserInfoBinding.root
    }

    fun movePrevFragment() {
        mainFragment.removeFragment(ShopSubFragmentName.MODIFY_USER_INFO_FRAGMENT)
    }


}