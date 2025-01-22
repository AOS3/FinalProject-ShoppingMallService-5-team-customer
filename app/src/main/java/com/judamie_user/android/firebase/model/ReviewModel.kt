package com.judamie_user.android.firebase.model

import com.judamie_user.android.firebase.vo.ReviewVO
import com.judamie_user.android.util.ReviewState

class ReviewModel {

    //리뷰 문서 ID
    var reviewDocumentID = ""


    //리뷰가 들어온 날짜
    var reviewWriteDate = ""


    //리뷰를 쓴 유저의 문서ID
    var reviewerUserDocumentID = ""


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
    var reviewState = ReviewState.REVIEW_STATE_NORMAL


    fun toReviewVO(): ReviewVO {
        val reviewVO = ReviewVO()

        reviewVO.reviewWriteDate = reviewWriteDate
        reviewVO.reviewerUserDocumentID = reviewerUserDocumentID
        reviewVO.reviewProductDocumentID = reviewProductDocumentID
        reviewVO.reviewPhoto = reviewPhoto
        reviewVO.reviewContent = reviewContent
        reviewVO.reviewRating = reviewRating
        reviewVO.reviewTimeStamp = reviewTimeStamp
        reviewVO.reviewState = reviewState.num

        return reviewVO
    }

}