package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.fragment.WishListFragment

class WishListViewModel(val wishListFragment: WishListFragment):ViewModel() {

    //textViewWishListCount
    val textViewWishListCountText = MutableLiveData("총 n개")

    //textViewEmptyWishList1
    val textViewEmptyWishListText1 = MutableLiveData("상품페이지의 찜 아이콘을 눌러")

    //textViewEmptyWishList2
    val textViewEmptyWishListText2 = MutableLiveData("찜 목록을 추가해보세요.")


}