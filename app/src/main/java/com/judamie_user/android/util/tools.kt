package com.judamie_user.android.util

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.judamie_user.android.R
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

        fun Int.formatToComma(): String {
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


        @JvmStatic
        @BindingAdapter("bind:imageUri")
        fun setImageUri(imageView: ImageView, imageUri: LiveData<Uri>?) {
            imageUri?.observeForever { uri ->
                Glide.with(imageView.context)
                    .load(uri)
                    .placeholder(R.drawable.liquor_24px) // 로딩 중 표시할 이미지
                    .error(R.drawable.liquor_24px) // 에러 발생 시 표시할 이미지
                    .into(imageView)
            }
        }

        @JvmStatic
        @BindingAdapter("app:textColorRes")
        fun setTextColorRes(textView: TextView, @ColorRes colorRes: Int?) {
            if (colorRes != null) {
                textView.setTextColor(ContextCompat.getColor(textView.context, colorRes))
            }
        }

        @JvmStatic
        @BindingAdapter("bind:backgroundDrawable")
        fun setBackgroundDrawable(view: View, drawable: Drawable?) {
            drawable?.let {
                view.background = it
            }
        }

        @JvmStatic
        @BindingAdapter("bind:textColorInt")
        fun setTextColor(button: Button, color: Int?) {
            color?.let {
                button.setTextColor(it)
            }
        }

    }

}