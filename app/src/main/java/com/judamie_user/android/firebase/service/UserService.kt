package com.judamie_user.android.firebase.service

import com.judamie_user.android.firebase.model.ProductModel
import com.judamie_user.android.firebase.repository.UserRepository
import com.judamie_user.android.firebase.vo.ProductVO

class UserService {
    companion object {

        suspend fun gettingUserNameByID(userDocumentID: String): String {
            var userName = UserRepository.gettingUserNameByID(userDocumentID)
            return userName
        }

        suspend fun gettingWishListByUserID(userDocumentID: String): MutableList<String> {
            val userWishList = UserRepository.gettingWishListByUserID(userDocumentID)

            return userWishList.toMutableList()
        }

        //해당 유저의 위시리스트에서 매개변수로들어온 제품을 추가하는메서드
        suspend fun addUserWishList(userDocumentID: String, productDocumentID: String) {
            UserRepository.addUserWishList(userDocumentID,productDocumentID)
        }

        //해당 유저의 위시리스트에서 매개변수로들어온 제품을 삭제하는메서드
        suspend fun deleteUserWishList(userDocumentID: String, productDocumentID: String) {
            UserRepository.deleteUserWishList(userDocumentID,productDocumentID)
        }


    }
}