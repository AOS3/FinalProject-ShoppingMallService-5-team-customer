package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.ui.fragment.LoginFragment

data class LoginViewModel(val loginFragment: LoginFragment) : ViewModel() {
    // textFieldLoginFragmentId - EditText - text
    val textFieldLoginFragmentIdEditTextText = MutableLiveData("")
    // textFieldLoginFragmentPw - EditText - text
    val textFieldLoginFragmentPwEditTextText = MutableLiveData("")
    // checkBoxLoginFragmentLoginAuto - checked
    val checkBoxLoginFragmentLoginAutoChecked = MutableLiveData(false)

    // buttonLoginFragmentSignIn - onClick
    fun buttonLoginFragmentSignInOnClick(){
        // 로그인 처리 메서드 호출
        loginFragment.proLogin()
    }

    // buttonLoginFragmentSignUp - onClick
    fun buttonLoginFragmentSignUpOnClick(){
        // 회원 가입 화면 이동 메서드 호출
        loginFragment.moveToRegisterStep1()
    }
}