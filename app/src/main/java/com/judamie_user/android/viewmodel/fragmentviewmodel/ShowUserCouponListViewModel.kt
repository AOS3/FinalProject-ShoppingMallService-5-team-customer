package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.subfragment.ShowUserCouponListFragment

class ShowUserCouponListViewModel(val showUserCouponListFragment: ShowUserCouponListFragment):ViewModel() {

    //materialToolbarShowUserCouponList
    companion object{
        // materialToolbarShowUserCouponList - onNavigationClickBarShowUserCouponList
        @JvmStatic
        @BindingAdapter("onNavigationClickBarShowUserCouponList")
        fun onNavigationClickBarShowUserCouponList(materialToolbar: MaterialToolbar, showUserCouponListFragment: ShowUserCouponListFragment){
            materialToolbar.setNavigationOnClickListener {
                // 이전으로 돌아간다.
                showUserCouponListFragment.movePrevFragment()
            }
        }
    }

}