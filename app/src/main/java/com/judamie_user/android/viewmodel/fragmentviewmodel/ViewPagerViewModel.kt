package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.fragment.ViewPagerFragment

data class ViewPagerViewModel(val viewPagerFragment: ViewPagerFragment) : ViewModel() {
    // 상품 개수
    // textViewHomeProductCount - text
    val textViewHomeProductCountText = MutableLiveData("")
}