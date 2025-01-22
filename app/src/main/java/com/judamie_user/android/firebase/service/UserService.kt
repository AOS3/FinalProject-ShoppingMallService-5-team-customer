package com.judamie_user.android.firebase.service

import com.judamie_user.android.firebase.repository.UserRepository

class UserService {
    companion object {

        // 가입하려는 아이디가 존재하는지 확인하는 메서드
        suspend fun checkJoinUserId(userId:String) : Boolean{
            // 아이디를 통해 사용자 정보를 가져온다.
            val userVoList = UserRepository.selectUserDataByUserId(userId)
            // 가져온 데이터가 있다면
            if(userVoList.isNotEmpty()){
                return false
            }
            // 가져온 데이터가 없다면
            else {
                return true
            }
        }

        // 가입하려는 이름이 존재하는지 확인하는 메서드
        suspend fun checkJoinUserName(userName:String) : Boolean{
            // 아이디를 통해 사용자 정보를 가져온다.
            val userVoList = UserRepository.selectUserDataByUserName(userName)
            // 가져온 데이터가 있다면
            if(userVoList.isNotEmpty()){
                return false
            }
            // 가져온 데이터가 없다면
            else {
                return true
            }
        }
    }
}