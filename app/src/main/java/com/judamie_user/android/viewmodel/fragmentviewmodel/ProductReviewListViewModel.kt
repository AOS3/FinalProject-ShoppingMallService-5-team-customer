package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.activity.FragmentName
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.ui.subfragment.ProductReviewListFragment

data class ProductReviewListViewModel(val productReviewListFragment: ProductReviewListFragment):ViewModel() {

    // toolbarProductReview - title
    val toolbarProductReviewTitle = MutableLiveData<String>()
    // toolbarProductReview - navigationIcon
    val toolbarProductReviewNavigationIcon = MutableLiveData<Int>()

    // textViewProductReviewCnt - text
    val textViewProductReviewCntText = MutableLiveData("")

    // 리뷰 쓰기
    // buttonProductReviewWrite - onClick
    fun buttonProductReviewWriteOnClick() {
        productReviewListFragment.moveToWriteReview()
    }

    companion object{
        // toolbarProductReview - onNavigationClickProductReviewList
        @JvmStatic
        @BindingAdapter("onNavigationClickProductReviewList")
        fun onNavigationClickProductReviewList(materialToolbar: MaterialToolbar, productReviewListFragment: ProductReviewListFragment){
            materialToolbar.setNavigationOnClickListener {
                // 이전으로 돌아간다.
                productReviewListFragment.mainFragment.removeFragment(ShopSubFragmentName.PRODUCT_REVIEW_LIST_FRAGMENT)
            }
        }
    }
}