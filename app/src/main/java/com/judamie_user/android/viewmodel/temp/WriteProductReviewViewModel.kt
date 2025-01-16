package com.judamie_user.android.viewmodel.temp

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.subfragment.ModifyUserInfoFragment
import com.judamie_user.android.ui.temp.WriteProductReviewFragment

class WriteProductReviewViewModel(val writeProductReviewFragment: WriteProductReviewFragment):ViewModel() {

    // materialToolbarWriteProductReview
    companion object {
        // materialToolbarWriteProductReview - onNavigationClickBarWriteProductReview
        @JvmStatic
        @BindingAdapter("onNavigationClickBarWriteProductReview")
        fun onNavigationClickBarWriteProductReview(
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
    val buttonWriteProductReviewAddPhotoText = MutableLiveData("사진 추가 \n (0/5)")

    //buttonWriteProductReviewAddPhoto
    fun buttonWriteProductReviewAddPhotoOnclick(){
        writeProductReviewFragment.buttonWriteProductReviewAddPhotoOnclick()
    }


    //buttonWriteProductReviewSaveReview
    fun buttonWriteProductReviewSaveReviewOnclick(){

    }
}