package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.fragment.WishListFragment

class WishListViewModel(val wishListFragment: WishListFragment):ViewModel() {

    //textViewWishListCount
    val textViewWishListCountText = MutableLiveData("")

    //textViewEmptyWishList1
    val textViewEmptyWishListText = MutableLiveData("상품페이지의 찜 아이콘을 눌러\n찜 목록을 추가해보세요.")


}