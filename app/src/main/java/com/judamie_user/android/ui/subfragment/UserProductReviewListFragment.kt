package com.judamie_user.android.ui.subfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentUserProductReviewListBinding
import com.judamie_user.android.viewmodel.fragmentviewmodel.UserProductReviewListViewModel


class UserProductReviewListFragment : Fragment() {
    lateinit var fragmentUserProductReviewListBinding:FragmentUserProductReviewListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentUserProductReviewListBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_product_review_list,container,false)
        fragmentUserProductReviewListBinding.viewModel = UserProductReviewListViewModel(this)
        fragmentUserProductReviewListBinding.lifecycleOwner = viewLifecycleOwner

        return fragmentUserProductReviewListBinding.root
    }




}