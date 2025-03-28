package com.judamie_user.android.firebase.service

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.google.firebase.firestore.FirebaseFirestore
import com.judamie_user.android.firebase.model.UserModel
import com.judamie_user.android.firebase.repository.UserRepository
import com.judamie_user.android.firebase.vo.UserVO
import com.judamie_user.android.util.LoginResult
import com.judamie_user.android.util.UserState
import kotlinx.coroutines.tasks.await

class UserService {
    companion object {

        // 사용자 정보를 추가하는 메서드
        fun addUserData(userModel: UserModel){
            // 데이터를 VO에 담아준다.
            val userVO = userModel.toUserVO()
            // 저장하는 메서드를 호출한다.
            UserRepository.addUserData(userVO)
        }


        // 장바구니에 상품 추가하기
        suspend fun addCartData(userModel : UserModel){
            // 데이터를 VO에 담아준다.
            val userVO = userModel.toUserVO()
            // 저장하는 메서드를 호출한다.
            UserRepository.addCartData(userVO,userModel.userDocumentID)
        }

        // 장바구니 상품 삭제하기
        suspend fun deleteUserCartData(userDocumentId: String, selectedIds: String){
            // Firebase에서 상품 삭제
            UserRepository.deleteUserCartData(userDocumentId, selectedIds)

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

        // 사용자 아이디를 통해 문서 id와 사용자 정보를 가져온다.
        // 사용자 아이디와 동일한 사용자의 정보 하나를 반환하는 메서드
        suspend fun selectUserDataByUserIdOne(userId:String) : UserModel{
            val tempMap = UserRepository.selectUserDataByUserIdOne(userId)
            val loginUserVo = tempMap["user_vo"] as UserVO
            val loginUserDocumentId = tempMap["user_document_id"] as String

            val loginUserModel = loginUserVo.toUserModel(loginUserDocumentId)

            return loginUserModel
        }

        // 사용자 문서 아이디를 통해 사용자 정보를 가져온다.
        suspend fun selectUserDataByUserDocumentIdOne(userDocumentId:String) : UserModel{
            val userVO = UserRepository.selectUserDataByUserDocumentIdOne(userDocumentId)
            val userModel = userVO.toUserModel(userDocumentId)

            Log.d("test101",userModel.userPickupLoc)
            return userModel
        }

        // 자동 로그인 토큰값 갱신 메서드
        suspend fun updateUserAutoLoginToken(context: Context, userDocumentId:String){
            // 새로운 토큰 값 발행
            val newToken = "${userDocumentId}${System.nanoTime()}"
            // SharedPreference 에 저장
            val pref = context.getSharedPreferences("LoginToken", Context.MODE_PRIVATE)
            pref.edit {
                putString("token", newToken)
            }
            // 서버에 저장
            UserRepository.updateUserAutoLoginToken(userDocumentId, newToken)
        }

        // 자동 로그인 토큰 값으로 사용자 정보를 가져오는 메서드
        suspend fun selectUserDataByLoginToken(loginToken:String) : UserModel?{
            val loginMap = UserRepository.selectUserDataByLoginToken(loginToken)
            if(loginMap == null){
                return null
            } else {
                val userDocumentId = loginMap["userDocumentId"] as String
                val userVO = loginMap["userVO"] as UserVO

                val userModel = userVO.toUserModel(userDocumentId)
                return userModel
            }
        }

        // 로그인 처리 메서드
        suspend fun checkLogin(loginUserId:String, loginUserPw:String) : LoginResult {
            // 로그인 결과
            var result = LoginResult.LOGIN_RESULT_SUCCESS

            // 입력한 아이디로 사용자 정보를 가져온다.
            val userVoList = UserRepository.selectUserDataByUserId(loginUserId)
            // 가져온 사용자 정보가 없다면
            if(userVoList.isEmpty()){
                result = LoginResult.LOGIN_RESULT_ID_NOT_EXIST
            } else {
                if(loginUserPw != userVoList[0].userPassword){
                    // 비밀번호가 다르다면
                    result = LoginResult.LOGIN_RESULT_PASSWORD_INCORRECT
                }
                // 탈퇴한 회원 이라면
                if(userVoList[0].userState == UserState.USER_STATE_SIGN_OUT.number){
                    result = LoginResult.LOGIN_RESULT_SIGN_OUT_MEMBER
                }
            }
            return result
        }
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

        // 탈퇴처리(해당 유저의 상태값을 변경한다)
        suspend fun updateUserState(userDocumentId:String, newState: UserState){
            UserRepository.updateUserState(userDocumentId,newState)
        }

        // 유저 쿠폰 삭제 처리 메서드
        suspend fun deleteCouponData(userDocumentId: String, couponDocumentId: String) {
            UserRepository.deleteCouponFromUser(userDocumentId, couponDocumentId)
        }

        // 사용자 데이터를 수정한다.
        suspend fun updateUserData(userModel: UserModel){
            UserRepository.updateUserCoupons(userModel.userDocumentID, userModel.userCoupons)
        }

        // 유저 장바구니 리스트 삭제 처리 메서드
        suspend fun deleteCartItem(userDocumentId: String, productDocumentId: String) {
            UserRepository.deleteCartItem(userDocumentId, productDocumentId)
        }

    }
}