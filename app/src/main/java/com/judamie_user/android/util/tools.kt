package com.judamie_user.android.util

import java.text.DecimalFormat

class tools {

    companion object {
        fun Int.formatToComma(): String {
            val formatter = DecimalFormat("#,###원")
            return formatter.format(this)
        }
    }

}