package com.judamie_user.android.firebase.service

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.judamie_user.android.firebase.model.ReviewModel
import com.judamie_user.android.firebase.repository.ReviewRepository
import kotlinx.coroutines.tasks.await

class ReviewService {
    companion object {
        // URI를 사용하여 이미지 데이터를 서버로 업로드하는 메서드
        suspend fun uploadImageFromUri(uri: Uri, serverFilePath: String) {
            ReviewRepository.uploadImage(uri,serverFilePath)
        }

        // 리뷰를 서버로 업로드는 하는 메서드
        suspend fun addReviewData(reviewModel: ReviewModel):String{
            //데이터를 vo에 담아준다
            val reviewVO = reviewModel.toReviewVO()
            //저장하는메서드를 호출함
            val reviewDocumentID = ReviewRepository.addReviewData(reviewVO)
            return reviewDocumentID
        }

        suspend fun addReviewDataInProduct(reviewDocumentID:String,productDocumentID:String){
            ReviewRepository.addReviewDataInProduct(reviewDocumentID,productDocumentID)
        }
    }
}
