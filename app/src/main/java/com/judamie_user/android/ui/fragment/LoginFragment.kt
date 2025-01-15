package com.judamie_user.android.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.google.android.material.textfield.TextInputEditText
import com.judamie_user.android.R
import com.judamie_user.android.activity.FragmentName
import com.judamie_user.android.activity.LoginActivity
import com.judamie_user.android.activity.ShopActivity
import com.judamie_user.android.databinding.FragmentLoginBinding
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

    // 로그인 처리 메서드
    fun proLogin() {
        fragmentLoginBinding.apply {
//            var hasError = false // 에러 상태 플래그
//
//            // 아이디 입력 검사
//            if (loginViewModel?.textFieldLoginFragmentIdEditTextText?.value?.isEmpty() == true) {
//                textFieldLoginFragmentId.error = "아이디를 입력해주세요"
//                hasError = true // 에러 플래그 설정
//            } else {
//                textFieldLoginFragmentId.error = null
//                hasError = false
//            }
//
//            // 비밀번호 입력 검사
//            if (loginViewModel?.textFieldLoginFragmentPwEditTextText?.value?.isEmpty() == true) {
//                textFieldLoginFragmentPw.error = "비밀번호를 입력해주세요"
//                hasError = true
//            } else {
//                textFieldLoginFragmentPw.error = null
//                hasError = false
//            }
//
//            // 에러가 있을 경우 로그인 성공 처리를 중단
//            if (hasError) {
//                return
//            } else {
//
//            }

            // 로그인 성공 처리
            val shopIntent = Intent(loginActivity, ShopActivity::class.java)
            startActivity(shopIntent)
            Log.d("test100", "로그인 성공")
            loginActivity.finish()

        }
    }

    // 회원 가입 화면으로 이동하는 메서드
    fun moveToRegisterStep1() {
        loginActivity.replaceFragment(FragmentName.REGISTER_STEP1_FRAGMENT, true, true, null)
    }

}