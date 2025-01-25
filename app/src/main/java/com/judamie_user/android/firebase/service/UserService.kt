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

    }
}