package com.judamie_user.android.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

class PictureHandler(private val contentResolver: ContentResolver) {

    /**
     * 회전 각도를 가져오는 메서드
     */
    fun getRotationDegree(uri: Uri): Int {
        val exif = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val inputStream = contentResolver.openInputStream(uri)
            ExifInterface(inputStream!!)
        } else {
            ExifInterface(uri.path!!)
        }

        return when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    /**
     * 이미지를 회전하는 메서드
     */
    fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix().apply { postRotate(degree.toFloat()) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * 이미지 크기 조정 메서드
     */
    fun resizeBitmap(targetWidth: Int, bitmap: Bitmap): Bitmap {
        val ratio = targetWidth.toDouble() / bitmap.width
        val targetHeight = (bitmap.height * ratio).toInt()
        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
    }

    /**
     * Uri로부터 Bitmap 가져오기
     */
    fun getBitmapFromUri(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        }
    }
}
