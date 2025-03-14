package com.judamie_user.android.viewmodel.fragmentviewmodel

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.subfragment.ShowUserOrderListFragment

class ShowUserOrderListViewModel(val showUserOrderListFragment: ShowUserOrderListFragment):ViewModel() {

    //materialToolbarShowUserOrderList
    companion object{
        //materialToolbarShowUserOrderList - onNavigationClickShowUserOrderList
        @JvmStatic
        @BindingAdapter("onNavigationClickShowUserOrderList")
        fun onNavigationClickShowUserOrderList(
            materialToolbar: MaterialToolbar,
            showUserOrderListFragment: ShowUserOrderListFragment
        ) {
            materialToolbar.setNavigationOnClickListener {
                showUserOrderListFragment.movePrevFragment()
            }
        }
    }

    //textViewNothingOrder visibility
    var textViewNothingOrderVisibility = MutableLiveData(View.GONE)

    //progressBarShowUserOrderList visibility
    var progressBarShowUserOrderListVisibility = MutableLiveData(View.GONE)

    //recyclerViewShowUserOrderList visibility
    val recyclerViewShowUserOrderListVisibility = MutableLiveData(View.INVISIBLE)





}