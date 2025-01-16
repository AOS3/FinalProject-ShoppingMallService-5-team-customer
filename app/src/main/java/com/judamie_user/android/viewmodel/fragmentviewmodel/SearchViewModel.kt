package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.fragment.SearchFragment

data class SearchViewModel(val searchFragment: SearchFragment) : ViewModel() {

    // toolbarSearch - title
    val toolbarSearchTitle = MutableLiveData<String>()
    // toolbarSearch - navigationIcon
    val toolbarSearchOnNavigationIcon = MutableLiveData<Int>()

    // 검색창
    // editTextSearchInput - Text
    val editTextSearchInputText = MutableLiveData("")

    // 상품 수량
    // textViewSearchProductCount - Text
    val textViewSearchProductCountText = MutableLiveData("")

    companion object{
        // toolbarSearch - onNavigationClickSearch
        @JvmStatic
        @BindingAdapter("onNavigationClickRegisterVerification")
        fun onNavigationClickSearch(materialToolbar: MaterialToolbar, searchFragment: SearchFragment){
            materialToolbar.setNavigationOnClickListener {
                // 이전으로 돌아가는 메서드 호출
                searchFragment.movePrevFragment()
            }
        }
    }

}