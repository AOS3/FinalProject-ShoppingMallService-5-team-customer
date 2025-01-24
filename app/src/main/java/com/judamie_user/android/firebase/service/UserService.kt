package com.judamie_user.android.firebase.service

import com.judamie_user.android.firebase.repository.UserRepository

class UserService {
    companion object {
        suspend fun gettingUserNameByID(userDocumentID: String): String {
            var userName = UserRepository.gettingUserNameByID(userDocumentID)
            return userName
        }
    }
}