package com.judamie_user.android.viewmodel.fragmentviewmodel

import android.widget.RatingBar
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.subfragment.WriteProductReviewFragment

class WriteProductReviewViewModel(val writeProductReviewFragment: WriteProductReviewFragment) : ViewModel() {

    companion object {
        // BindingAdapter for MaterialToolbar navigation click
        @JvmStatic
        @BindingAdapter("onNavigationClickWriteProductReview")
        fun onNavigationClickWriteProductReview(
            materialToolbar: MaterialToolbar,
            writeProductReviewFragment: WriteProductReviewFragment
        ) {
            materialToolbar.setNavigationOnClickListener {
                writeProductReviewFragment.movePrevFragment()
            }
        }

        // BindingAdapter for RatingBar
        @JvmStatic
        @BindingAdapter("onRatingChanged")
        fun setOnRatingBarChangeListener(ratingBar: RatingBar, viewModel: WriteProductReviewViewModel) {
            ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
                viewModel.ratingBarWriteProductReviewRate.value = rating
            }
        }
    }

    // LiveData for product name
    val textViewWriteProductReviewProductNameText = MutableLiveData("textViewWriteProductReviewProductName")

    // LiveData for product seller
    val textViewWriteProductReviewProductSellerText = MutableLiveData("textViewWriteProductReviewProductSeller")

    // LiveData for rating bar value
    val ratingBarWriteProductReviewRate = MutableLiveData<Float>()

    // LiveData for review content
    val textInputLayoutWriteProductReviewContentText = MutableLiveData("textInputLayoutWriteProductReviewContent")

    // LiveData for add photo button text
    val buttonWriteProductReviewAddPhotoText = MutableLiveData("")

    // Add photo button click handler
    fun buttonWriteProductReviewAddPhotoOnclick() {
        writeProductReviewFragment.buttonWriteProductReviewAddPhotoOnclick()
    }

    // Save review button click handler
    fun buttonWriteProductReviewSaveReviewOnclick() {
        writeProductReviewFragment.saveReview()
    }
}
