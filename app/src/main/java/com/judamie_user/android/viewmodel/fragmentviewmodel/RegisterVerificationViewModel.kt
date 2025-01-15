package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.activity.FragmentName
import com.judamie_user.android.ui.fragment.LoginFragment
import com.judamie_user.android.ui.fragment.RegisterVerificationFragment

data class RegisterVerificationViewModel(val registerVerificationFragment: RegisterVerificationFragment) :ViewModel() {
    // toolbarRegisterVerification - title
    val toolbarRegisterVerificationTitle = MutableLiveData<String>()
    // toolbarRegisterVerification - navigationIcon
    val toolbarRegisterVerificationNavigationIcon = MutableLiveData<Int>()
    // textFieldRegisterVerificationPhoneNo - EditText - text
    val textFieldRegisterVerificationPhoneNoEditTextText = MutableLiveData("")
    // textFieldRegisterVerificationVerificationNo - EditText - text
    val textFieldRegisterVerificationVerificationNoEditTextText = MutableLiveData("")


    // buttonRegisterVerificationRequestVerification - onClick
    fun buttonRegisterVerificationRequestVerificationOnClick(){

    }

    // buttonRegisterVerificationCheckInfo - onClick
    fun buttonRegisterVerificationCheckInfoOnClick(){

    }

    // buttonRegisterVerificationSubmit - onClick
    fun buttonRegisterVerificationSubmitOnClick(){
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