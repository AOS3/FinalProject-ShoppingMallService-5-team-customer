package com.judamie_user.android.viewmodel.fragmentviewmodel

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.activity.FragmentName
import com.judamie_user.android.ui.fragment.LoginFragment
import com.judamie_user.android.ui.fragment.RegisterVerificationFragment

class RegisterVerificationViewModel(val registerVerificationFragment: RegisterVerificationFragment) :ViewModel() {
    // toolbarRegisterVerification - title
    val toolbarRegisterVerificationTitle = MutableLiveData<String>()
    // toolbarRegisterVerification - navigationIcon
    val toolbarRegisterVerificationNavigationIcon = MutableLiveData<Int>()
    // textFieldRegisterVerificationPhoneNo - EditText - text
    val textFieldRegisterVerificationPhoneNoEditTextText = MutableLiveData("")
    // textFieldRegisterVerificationVerificationNo - EditText - text
    val textFieldRegisterVerificationVerificationNoEditTextText = MutableLiveData("")


    // 인증 요청 버튼
    // buttonRegisterVerificationRequestVerification - onClick
    fun buttonRegisterVerificationRequestVerificationOnClick(){
        val phoneNumber1 =textFieldRegisterVerificationPhoneNoEditTextText.value
        if (phoneNumber1.isNullOrEmpty()) {
            registerVerificationFragment.fragmentRegisterVerificationBinding.textFieldRegisterVerificationFragmentPhoneNo.error = "전화번호를 입력해주세요"
        } else {
            val phoneNumber = "+82" + phoneNumber1
            registerVerificationFragment.sendVerificationCode(phoneNumber)
        }
    }

    // 인증 확인 버튼
    // buttonRegisterVerificationCheckInfo - onClick
    fun buttonRegisterVerificationCheckInfoOnClick(view: View) {
        registerVerificationFragment.verificationCheck()
    }

    // buttonRegisterVerificationSubmit - onClick
    fun buttonRegisterVerificationSubmitOnClick(view: View){
        // 가입 완료 처리 메서드 호출
        registerVerificationFragment.proUserJoin()
    }

    companion object{
        // toolbarRegisterVerification - onNavigationClickRegisterVerification
        @JvmStatic
        @BindingAdapter("onNavigationClickRegisterVerification")
        fun onNavigationClickRegisterVerification(materialToolbar: MaterialToolbar, registerVerificationFragment: RegisterVerificationFragment){
            materialToolbar.setNavigationOnClickListener {
                // 이전으로 돌아간다.
                registerVerificationFragment.loginActivity.removeFragment(FragmentName.REGISTER_VERIFICATION_FRAGMENT)
            }
        }
    }
}