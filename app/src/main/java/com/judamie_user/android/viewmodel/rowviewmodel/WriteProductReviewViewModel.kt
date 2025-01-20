package com.judamie_user.android.viewmodel.rowviewmodel

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.temp.WriteProductReviewFragment

class WriteProductReviewViewModel(val writeProductReviewFragment: WriteProductReviewFragment):ViewModel() {

    // materialToolbarWriteProductReview
    companion object {
        // materialToolbarWriteProductReview - onNavigationClickWriteProductReview
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
    }



    //textViewWriteProductReviewProductName
    val textViewWriteProductReviewProductNameText = MutableLiveData("textViewWriteProductReviewProductName")

    //textViewWriteProductReviewProductSeller
    val textViewWriteProductReviewProductSellerText = MutableLiveData("textViewWriteProductReviewProductSeller")

    //ratingBarWriteProductReviewRate

    //textInputLayoutWriteProductReviewContent
    val textInputLayoutWriteProductReviewContentText = MutableLiveData("textInputLayoutWriteProductReviewContent")

    //buttonWriteProductReviewAddPhoto(Text)
    val buttonWriteProductReviewAddPhotoText = MutableLiveData("")

    //buttonWriteProductReviewAddPhoto
    fun buttonWriteProductReviewAddPhotoOnclick(){
        writeProductReviewFragment.buttonWriteProductReviewAddPhotoOnclick()
    }


    //buttonWriteProductReviewSaveReview
    fun buttonWriteProductReviewSaveReviewOnclick(){
        writeProductReviewFragment.saveReview()
    }
}