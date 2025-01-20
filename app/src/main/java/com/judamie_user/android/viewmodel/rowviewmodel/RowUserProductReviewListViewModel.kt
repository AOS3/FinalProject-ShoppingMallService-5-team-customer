package com.judamie_user.android.viewmodel.rowviewmodel

import androidx.lifecycle.MutableLiveData
import com.judamie_user.android.ui.subfragment.UserProductReviewListFragment

data class RowUserProductReviewListViewModel(val userProductReviewListFragment: UserProductReviewListFragment) {
    // rowTextViewUserProductReviewProductName - text
    val rowTextViewUserProductReviewProductNameText = MutableLiveData("")

    // rowTextViewUserProductReviewContent - text
    val rowTextViewUserProductReviewContentText = MutableLiveData("")

    // rowTextViewUserProductReviewDate - text
    val rowTextViewUserProductReviewDateText = MutableLiveData("")

    // rowRecyclerViewUserProductReviewAttach - recyclerView
    val rowRecyclerViewUserProductReviewAttachRecyclerView = MutableLiveData<List<String>>()

    // rowRatingBarUserProductReview - rating
    val rowRatingBarUserProductReviewRating = MutableLiveData(0f)


}