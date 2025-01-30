package com.judamie_user.android.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import java.text.DecimalFormat

class tools {

    companion object {
        fun Int.formatToComma(): String {
            val formatter = DecimalFormat("#,###원")
            return formatter.format(this)
        }
    }

    object BindingAdapters {

        @JvmStatic
        @BindingAdapter("bind:srcCompat")
        fun setImageSrcCompat(imageView: ImageView, imageRes: LiveData<Int>?) {
            imageRes?.value?.let { resId ->
                imageView.setImageResource(resId)
            }
        }
    }

}