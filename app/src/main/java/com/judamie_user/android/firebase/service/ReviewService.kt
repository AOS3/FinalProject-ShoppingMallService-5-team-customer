package com.judamie_user.android.firebase.service

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.judamie_user.android.firebase.model.ReviewModel
import com.judamie_user.android.firebase.repository.ReviewRepository
import com.judamie_user.android.firebase.vo.ReviewVO
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

        // 리뷰id를 제품 데이터에 넣는 메서드
        suspend fun addReviewDataInProduct(reviewDocumentID:String,productDocumentID:String){
            ReviewRepository.addReviewDataInProduct(reviewDocumentID,productDocumentID)
        }

        //리뷰 documentID로 리뷰를 가져오는 메서드
        suspend fun gettingReviewByID(reviewDocumentID:String): ReviewModel {
            val reviewVO = ReviewRepository.gettingReviewByID(reviewDocumentID)
            val reviewModel = reviewVO.toReviewModel(reviewDocumentID)

            return reviewModel
        }

        //리뷰 이미지 데이터를 가져온다
        suspend fun gettingReviewImage(imageFileName:String) : Uri{
            val imageUri = ReviewRepository.gettingReviewImage(imageFileName)
            return imageUri
        }

        //유저 userDocumentID로 한 유저의 리뷰 목록을 불러오는 메서드
        suspend fun gettingReviewListByOneUser(userDocumentID: String): MutableList<ReviewModel> {
            val reviewList = ReviewRepository.gettingReviewListByOneUser(userDocumentID)

            val reviewModelList = mutableListOf<ReviewModel>()
            reviewList.forEach {
                reviewModelList.add(it.toReviewModel(userDocumentID))
            }
            return reviewModelList
        }
    }
}
