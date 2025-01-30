package com.judamie_user.android.ui.subfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.judamie_user.android.R
import com.judamie_user.android.activity.ShopActivity
import com.judamie_user.android.databinding.FragmentCompletedPaymentBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.viewmodel.fragmentviewmodel.CompletedPaymentFragmentViewModel
import com.judamie_user.android.viewmodel.fragmentviewmodel.RegisterStep1ViewModel

class CompletedPaymentFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentCompletedPaymentBinding: FragmentCompletedPaymentBinding
    lateinit var shopActivity: ShopActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentCompletedPaymentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_completed_payment, container, false)
        fragmentCompletedPaymentBinding.completePaymentViewModel = CompletedPaymentFragmentViewModel(this@CompletedPaymentFragment)
        fragmentCompletedPaymentBinding.lifecycleOwner = this@CompletedPaymentFragment


        return fragmentCompletedPaymentBinding.root
    }

    // 제품 주문 내역 화면으로 이동하는 메서드
    fun moveToUserOrder() {
        mainFragment.removeFragment(ShopSubFragmentName.COMPLETE_PAYMENT_FRAGMENT)
        mainFragment.replaceFragment(ShopSubFragmentName.SHOW_USER_ORDER_LIST_FRAGMENT, true, true, null)
    }

}