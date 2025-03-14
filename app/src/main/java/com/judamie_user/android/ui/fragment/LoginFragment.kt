package com.judamie_user.android.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.judamie_user.android.R
import com.judamie_user.android.activity.FragmentName
import com.judamie_user.android.activity.LoginActivity
import com.judamie_user.android.activity.ShopActivity
import com.judamie_user.android.databinding.FragmentLoginBinding
import com.judamie_user.android.firebase.service.UserService
import com.judamie_user.android.util.LoginResult
import com.judamie_user.android.viewmodel.fragmentviewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var loginActivity: LoginActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        fragmentLoginBinding.loginViewModel = LoginViewModel(this@LoginFragment)
        fragmentLoginBinding.lifecycleOwner = this@LoginFragment

        loginActivity = activity as LoginActivity

        return fragmentLoginBinding.root
    }

    // 회원 가입 화면 이동 메서드
    fun moveToRegisterStep1() {
        loginActivity.replaceFragment(FragmentName.REGISTER_STEP1_FRAGMENT, true, true, null)
    }

    // 로그인 처리 메서드
    fun proLogin() {
        fragmentLoginBinding.apply {
            var hasError = false // 에러 상태 플래그

            // 아이디 입력 검사
            if (loginViewModel?.textFieldLoginFragmentIdEditTextText?.value?.isEmpty() == true) {
                textFieldLoginFragmentId.error = "아이디를 입력해주세요"
                hasError = true // 에러 플래그 설정
            } else {
                textFieldLoginFragmentId.error = null
                hasError = false
            }

            // 비밀번호 입력 검사
            if (loginViewModel?.textFieldLoginFragmentPwEditTextText?.value?.isEmpty() == true) {
                textFieldLoginFragmentPw.error = "비밀번호를 입력해주세요"
                hasError = true
            } else {
                textFieldLoginFragmentPw.error = null
                hasError = false
            }

            // 에러가 있을 경우 로그인 성공 처리를 중단
            if (hasError) {
                return
            } else {

            }

            // 사용자가 입력한 아이디와 비밀번호
            val loginUserId = loginViewModel?.textFieldLoginFragmentIdEditTextText?.value!!
            val loginUserPw = loginViewModel?.textFieldLoginFragmentPwEditTextText?.value!!

            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO) {
                    UserService.checkLogin(loginUserId, loginUserPw)
                }
                // 로그인 결과를 가져온다.
                val loginResult = work1.await()
                // Log.d("test100", loginResult.str)

                // 로그인 결과로 분기
                when(loginResult) {
                    // 등록되지 않은 아이디
                    LoginResult.LOGIN_RESULT_ID_NOT_EXIST -> {
                        textFieldLoginFragmentId.error = "등록되지않은 아이디입니다."
                        loginActivity.showSoftInput(textFieldLoginFragmentId.editText!!)
                    }
                    // 비밀번호 틀림
                    LoginResult.LOGIN_RESULT_PASSWORD_INCORRECT -> {
                        textFieldLoginFragmentPw.error = "잘못된 비밀번호입니다."
                        loginActivity.showSoftInput(textFieldLoginFragmentPw.editText!!)
                    }
                    // 탈퇴 회원
                    LoginResult.LOGIN_RESULT_SIGN_OUT_MEMBER -> {
                        loginActivity.showMessageDialog("로그인 실패", "탈퇴한 회원 입니다", "확인") {
                            loginViewModel?.textFieldLoginFragmentIdEditTextText?.value = ""
                            loginViewModel?.textFieldLoginFragmentPwEditTextText?.value = ""
                            loginActivity.showSoftInput(textFieldLoginFragmentId.editText!!)
                        }
                    }
                    // 로그인 성공
                    LoginResult.LOGIN_RESULT_SUCCESS -> {
                        // 로그인 사용자 정보를 가져온다.
                        val work2 = async(Dispatchers.IO) {
                            UserService.selectUserDataByUserIdOne(loginUserId)
                        }
                        val loginUserModel = work2.await()

                        // 자동 로그인 체크 시,
                        if (loginViewModel?.checkBoxLoginFragmentLoginAutoChecked?.value!!) {
                            CoroutineScope(Dispatchers.Main).launch {
                                val work3 = async(Dispatchers.IO) {
                                    UserService.updateUserAutoLoginToken(loginActivity, loginUserModel.userDocumentID)
                                }
                                work3.join()
                                Log.d("test100", "자동 로그인 토큰 업데이트 완료")
                            }
                        }

                        // LoginActivity 종료, ShopActivity 실행
                        val shopIntent = Intent(loginActivity, ShopActivity::class.java)
                        shopIntent.putExtra("user_document_id", loginUserModel.userDocumentID)
                        shopIntent.putExtra("user_name", loginUserModel.userName)
                        startActivity(shopIntent)
                        loginActivity.finish()
                    }
                }
            }
        }
    }

}