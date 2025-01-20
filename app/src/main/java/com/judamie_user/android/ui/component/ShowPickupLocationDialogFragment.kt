package com.judamie_user.android.ui.component

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentShowPickupLocationDialogBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.viewmodel.componentviewmodel.ShowPickupLocationDialogViewModel

class ShowPickupLocationDialogFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentShowPickupLocationDialogBinding:FragmentShowPickupLocationDialogBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShowPickupLocationDialogBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_show_pickup_location_dialog,container,false)
        fragmentShowPickupLocationDialogBinding.showPickupLocationDialogViewModel = ShowPickupLocationDialogViewModel(this)
        fragmentShowPickupLocationDialogBinding.lifecycleOwner = viewLifecycleOwner



        return fragmentShowPickupLocationDialogBinding.root
    }

    //전화걸기
    fun buttonCalling() {
        TODO("Not yet implemented")
    }

    //다이얼로그 닫기
    fun buttonClose() {
        TODO("Not yet implemented")
    }

}