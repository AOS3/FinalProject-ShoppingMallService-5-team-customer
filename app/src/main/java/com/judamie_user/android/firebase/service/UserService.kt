package com.judamie_user.android.firebase.service

import com.judamie_user.android.firebase.model.UserModel
import com.judamie_user.android.firebase.repository.UserRepository

class UserService {
    companion object {

        // 사용자 정보를 추가하는 메서드
        fun addUserData(userModel: UserModel){
            // 데이터를 VO에 담아준다.
            val userVO = userModel.toUserVO()
            // 저장하는 메서드를 호출한다.
            UserRepository.addUserData(userVO)
        }

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