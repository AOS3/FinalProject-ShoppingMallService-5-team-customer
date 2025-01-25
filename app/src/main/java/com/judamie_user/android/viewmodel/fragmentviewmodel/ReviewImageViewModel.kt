package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.subfragment.ReviewImageFragment

class ReviewImageViewModel(val reviewImageFragment: ReviewImageFragment):ViewModel() {

    //imageButtonReviewImageBack - onclick
    fun imageButtonReviewImageBackOnClick(){
        reviewImageFragment.movePrevFragment()
    }
}