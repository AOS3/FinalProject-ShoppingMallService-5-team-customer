package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.ui.subfragment.ProductReviewListFragment
import com.judamie_user.android.ui.subfragment.UserProductReviewListFragment

class UserProductReviewListViewModel(val userProductReviewListFragment: UserProductReviewListFragment):ViewModel() {

    // toolbarUserProductReview - title
    val toolbarUserProductReviewTitle = MutableLiveData<String>()
    // toolbarUserProductReview - navigationIcon
    val toolbarUserProductReviewNavigationIcon = MutableLiveData<Int>()

    // textViewUserProductReviewCnt - text
    val textViewUserProductReviewCntText = MutableLiveData("임시데이터")

    companion object{
        // toolbarUserProductReview - onNavigationClickUserProductReviewList
        @JvmStatic
        @BindingAdapter("onNavigationClickUserProductReviewList")
        fun onNavigationClickUserProductReviewList(materialToolbar: MaterialToolbar, userProductReviewListFragment: UserProductReviewListFragment){
            materialToolbar.setNavigationOnClickListener {
                // 이전으로 돌아간다.
                userProductReviewListFragment.mainFragment.removeFragment(ShopSubFragmentName.USER_PRODUCT_REVIEW_FRAGMENT)
            }
        }
    }
}