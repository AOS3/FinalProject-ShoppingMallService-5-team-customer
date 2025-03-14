package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.activity.FragmentName
import com.judamie_user.android.ui.fragment.LoginFragment
import com.judamie_user.android.ui.fragment.RegisterStep1Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class RegisterStep1ViewModel(val registerStep1Fragment: RegisterStep1Fragment) : ViewModel() {

    // toolbarRegisterStep1 - title
    val toolbarRegisterStep1Title = MutableLiveData<String>()
    // toolbarRegisterStep1 - navigationIcon
    val toolbarRegisterStep1NavigationIcon = MutableLiveData<Int>()
    // textFieldRegisterStep1Id - EditText - text
    val textFieldRegisterStep1IdEditTextText = MutableLiveData("")
    // textFieldRegisterStep1Pw1 - EditText - text
    val textFieldRegisterStep1Pw1EditTextText = MutableLiveData("")
    // textFieldRegisterStep1Pw2 - EditText - text
    val textFieldRegisterStep1Pw2EditTextText = MutableLiveData("")
    // textFieldRegisterStep1FragmentName - EditText - Text
    val textFieldRegisterStep1FragmentNameEditTextText = MutableLiveData("")


    // buttonLoginNextStep - onClick
    fun buttonLoginNextStepOnClick() {
        registerStep1Fragment.apply {

            // 기존 에러 메시지 초기화
            fragmentRegisterStep1Binding.textFieldRegisterStep1Id.error = null
            fragmentRegisterStep1Binding.textFieldRegisterStep1Pw1.error = null
            fragmentRegisterStep1Binding.textFieldRegisterStep1Pw2.error = null
            fragmentRegisterStep1Binding.textFieldRegisterStep1FragmentName.error = null

            // 입력 요소 검사
            if (textFieldRegisterStep1IdEditTextText.value?.isEmpty()!!) {
                // 아이디 입력란에 에러 메시지 표시
                fragmentRegisterStep1Binding.textFieldRegisterStep1Id.error = "아이디를 입력해주세요"
                loginActivity.showSoftInput(fragmentRegisterStep1Binding.textFieldRegisterStep1Id.editText!!)
                // 2초 뒤에 에러 메시지 제거
                // clearErrorAfterDelay(fragmentRegisterStep1Binding.textFieldRegisterStep1Id, 2000)
                return
            }

            if (textFieldRegisterStep1IdEditTextText.value!!.length > 15) {
                // 아이디 입력란에 에러 메시지 표시
                fragmentRegisterStep1Binding.textFieldRegisterStep1Id.error = "아이디는 15자 이내로 입력해주세요"
                loginActivity.showSoftInput(fragmentRegisterStep1Binding.textFieldRegisterStep1Id.editText!!)
                // 2초 뒤에 에러 메시지 제거
                // clearErrorAfterDelay(fragmentRegisterStep1Binding.textFieldRegisterStep1Id, 2000)
                return
            }

            if (textFieldRegisterStep1Pw1EditTextText.value?.isEmpty()!!) {
                // 비밀번호 입력란에 에러 메시지 표시
                fragmentRegisterStep1Binding.textFieldRegisterStep1Pw1.error = "비밀번호를 입력해주세요"
                loginActivity.showSoftInput(fragmentRegisterStep1Binding.textFieldRegisterStep1Pw1.editText!!)
                // 2초 뒤에 에러 메시지 제거
                // clearErrorAfterDelay(fragmentRegisterStep1Binding.textFieldRegisterStep1Pw1, 2000)
                return
            }

            if (textFieldRegisterStep1Pw2EditTextText.value?.isEmpty()!!) {
                // 비밀번호 확인 입력란에 에러 메시지 표시
                fragmentRegisterStep1Binding.textFieldRegisterStep1Pw2.error = "비밀번호를 입력해주세요"
                loginActivity.showSoftInput(fragmentRegisterStep1Binding.textFieldRegisterStep1Pw2.editText!!)
                // 2초 뒤에 에러 메시지 제거
                // clearErrorAfterDelay(fragmentRegisterStep1Binding.textFieldRegisterStep1Pw2, 2000)
                return
            }

            if (textFieldRegisterStep1Pw1EditTextText.value != textFieldRegisterStep1Pw2EditTextText.value) {
                // 비밀번호 불일치 에러 메시지 표시
                fragmentRegisterStep1Binding.textFieldRegisterStep1Pw1.error = "비밀번호가 다릅니다"
                fragmentRegisterStep1Binding.textFieldRegisterStep1Pw2.error = "비밀번호가 다릅니다"
                fragmentRegisterStep1Binding.registerStep1ViewModel?.textFieldRegisterStep1Pw1EditTextText?.value = ""
                fragmentRegisterStep1Binding.registerStep1ViewModel?.textFieldRegisterStep1Pw2EditTextText?.value = ""
                loginActivity.showSoftInput(fragmentRegisterStep1Binding.textFieldRegisterStep1Pw1.editText!!)
                // 2초 뒤에 에러 메시지 제거
                // clearErrorAfterDelay(fragmentRegisterStep1Binding.textFieldRegisterStep1Pw1, 2000)
                // 2초 뒤에 에러 메시지 제거
                // clearErrorAfterDelay(fragmentRegisterStep1Binding.textFieldRegisterStep1Pw2, 2000)
                return
            }

            // 입력 요소 검사
            if (textFieldRegisterStep1FragmentNameEditTextText.value?.isEmpty()!!) {
                // 아이디 입력란에 에러 메시지 표시
                fragmentRegisterStep1Binding.textFieldRegisterStep1FragmentName.error = "이름을 입력해주세요"
                loginActivity.showSoftInput(fragmentRegisterStep1Binding.textFieldRegisterStep1FragmentName.editText!!)
                // 2초 뒤에 에러 메시지 제거
                // clearErrorAfterDelay(fragmentRegisterStep1Binding.textFieldRegisterStep1FragmentName, 2000)
                return
            }

            if (textFieldRegisterStep1FragmentNameEditTextText.value!!.length > 15) {
                // 이름 길이 초과 에러 메시지 표시
                fragmentRegisterStep1Binding.textFieldRegisterStep1FragmentName.error = "이름은 15자 이내로 입력해주세요"
                loginActivity.showSoftInput(fragmentRegisterStep1Binding.textFieldRegisterStep1FragmentName.editText!!)
                // 2초 뒤에 에러 메시지 제거
                // clearErrorAfterDelay(fragmentRegisterStep1Binding.textFieldRegisterStep1FragmentName, 2000)
                return
            }


            // 중복 확인
            checkRegisterIdName()


            // 다음 화면으로 이동
            // moveToUserVerification()
        }
    }


    companion object{
        // toolbarRegisterStep1 - onNavigationClickRegisterStep1
        @JvmStatic
        @BindingAdapter("onNavigationClickRegisterStep1")
        fun onNavigationClickRegisterStep1(materialToolbar: MaterialToolbar, registerStep1Fragment: RegisterStep1Fragment){
            materialToolbar.setNavigationOnClickListener {
                // 이전으로 돌아간다.
                registerStep1Fragment.loginActivity.removeFragment(FragmentName.REGISTER_STEP1_FRAGMENT)
            }
        }
    }

}