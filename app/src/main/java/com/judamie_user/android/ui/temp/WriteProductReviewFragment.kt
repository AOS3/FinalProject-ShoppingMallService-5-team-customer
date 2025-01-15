package com.judamie_user.android.ui.temp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentWriteProductReviewBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.viewmodel.temp.WriteProductReviewViewModel


class WriteProductReviewFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentWriteProductReviewBinding: FragmentWriteProductReviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentWriteProductReviewBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_write_product_review,container,false)
        fragmentWriteProductReviewBinding.writeProductReviewViewModel = WriteProductReviewViewModel(this)
        fragmentWriteProductReviewBinding.lifecycleOwner = viewLifecycleOwner


        return fragmentWriteProductReviewBinding.root
    }


}