package com.judamie_user.android.firebase.repository

import android.net.Uri
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.judamie_user.android.firebase.vo.ReviewVO
import kotlinx.coroutines.tasks.await
import java.io.File

class ReviewRepository {
    companion object{
        // 리뷰의 이미지 데이터를 서버로 업로드 하는 메서드
        suspend fun uploadImage(uri: Uri, serverFilePath: String){
            val firebaseStorage = FirebaseStorage.getInstance()
            val childReference = firebaseStorage.reference.child(serverFilePath)
            childReference.putFile(uri).await()
        }

        // 리뷰를 서버로 업로드는 하는 메서드
        suspend fun addReviewData(reviewVO: ReviewVO):String{
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("ReviewData")
            val documentReference = collectionReference.add(reviewVO).await()
            return documentReference.id // 새로 생성된 문서의 ID 반환

        }

        //리뷰의 documentID를 productData에 넣는 메서드
        suspend fun addReviewDataInProduct(reviewDocumentID:String,productDocumentID:String){
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("productData")
            val documentReference = collectionReference.document(productDocumentID)

            documentReference.update("productReview", FieldValue.arrayUnion(reviewDocumentID))
                .await()
        }



    }
}