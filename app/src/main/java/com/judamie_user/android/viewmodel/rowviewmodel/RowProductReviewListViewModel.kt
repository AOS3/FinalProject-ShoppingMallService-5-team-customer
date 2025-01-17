package com.judamie_user.android.viewmodel.rowviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.subfragment.ProductReviewListFragment

data class RowProductReviewListViewModel(val productReviewListFragment: ProductReviewListFragment) : ViewModel() {
    // rowTextViewProductReviewName - text
    val rowTextViewProductReviewNameText = MutableLiveData("")

    // rowTextViewProductReviewContent - text
    val rowTextViewProductReviewContentText = MutableLiveData("")

    // rowTextViewProductReviewDate - text
    val rowTextViewProductReviewDateText = MutableLiveData("")

    // rowRecyclerViewProductReviewAttach - recyclerView
    val rowRecyclerViewProductReviewAttachRecyclerView = MutableLiveData<List<String>>()

    // rowRatingBarProductReview - rating
    val rowRatingBarProductReviewRating = MutableLiveData(0f)

    // rowTextViewProductReviewName - onClick
    fun rowTextViewProductReviewNameOnClick() {

    }
}