package com.judamie_user.android.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.google.firebase.Timestamp
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class tools {

    companion object {
        fun Double.formatToComma(): String {
            val formatter = DecimalFormat("#,###원")
            return formatter.format(this)
        }

        fun Long.toFormattedDate(): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // 원하는 형식 지정
            return formatter.format(Date(this)) // Long을 Date로 변환 후 포맷 적용
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