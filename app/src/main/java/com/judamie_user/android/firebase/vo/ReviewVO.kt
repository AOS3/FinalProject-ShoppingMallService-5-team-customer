package com.judamie_user.android.firebase.vo

import com.judamie_user.android.firebase.model.ReviewModel
import com.judamie_user.android.util.ReviewState

class ReviewVO {

    //리뷰가 들어온 날짜
    var reviewWriteDate = ""

    //리뷰를 쓴 유저의 문서ID
    var reviewerUserDocumentID = ""

    //리뷰를 쓴 유저의 이름
    var reviewerName = ""

    //리뷰가 들어간 제품의 문서ID
    var reviewProductDocumentID = ""

    //리뷰에들어간 사진 리스트
    var reviewPhoto = mutableListOf<String>()

    //리뷰 본문
    var reviewContent = ""

    //리뷰 별점
    var reviewRating = 0.0F

    //리뷰가들어간 시간
    var reviewTimeStamp = 0L

    //리뷰상태
    var reviewState = 1

    fun toReviewModel(reviewDocumentID:String) :ReviewModel{
        val reviewModel = ReviewModel()

        reviewModel.reviewDocumentID = reviewDocumentID
        reviewModel.reviewWriteDate = reviewWriteDate
        reviewModel.reviewerUserDocumentID = reviewerUserDocumentID
        reviewModel.reviewerName = reviewerName
        reviewModel.reviewProductDocumentID = reviewProductDocumentID
        reviewModel.reviewPhoto = reviewPhoto
        reviewModel.reviewContent = reviewContent
        reviewModel.reviewRating = reviewRating
        reviewModel.reviewTimeStamp = reviewTimeStamp

        when(reviewState){
            ReviewState.REVIEW_STATE_NORMAL.num-> reviewModel.reviewState = ReviewState.REVIEW_STATE_NORMAL
            ReviewState.REVIEW_STATE_DELETE.num-> reviewModel.reviewState = ReviewState.REVIEW_STATE_DELETE
        }

        return reviewModel
    }
}