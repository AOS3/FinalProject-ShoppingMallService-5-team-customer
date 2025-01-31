package com.judamie_user.android.ui.fragment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.textfield.TextInputLayout
import com.judamie_user.android.R
import com.judamie_user.android.activity.FragmentName
import com.judamie_user.android.activity.LoginActivity
import com.judamie_user.android.databinding.FragmentLoginBinding
import com.judamie_user.android.databinding.FragmentRegisterStep1Binding
import com.judamie_user.android.firebase.service.UserService
import com.judamie_user.android.viewmodel.fragmentviewmodel.LoginViewModel
import com.judamie_user.android.viewmodel.fragmentviewmodel.RegisterStep1ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RegisterStep1Fragment : Fragment() {

    lateinit var fragmentRegisterStep1Binding: FragmentRegisterStep1Binding
    lateinit var loginActivity: LoginActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentRegisterStep1Binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_register_step1, container, false)
        fragmentRegisterStep1Binding.registerStep1ViewModel = RegisterStep1ViewModel(this@RegisterStep1Fragment)
        fragmentRegisterStep1Binding.lifecycleOwner = this@RegisterStep1Fragment

        loginActivity = activity as LoginActivity

        // 툴바를 구성하는 메서드를 호출
        settingToolbar()

        return fragmentRegisterStep1Binding.root
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        fragmentRegisterStep1Binding.registerStep1ViewModel?.apply {
            // 타이틀
            toolbarRegisterStep1Title.value = "회원 가입"
            // 네비게이션 아이콘
            toolbarRegisterStep1NavigationIcon.value = R.drawable.arrow_back_ios_24px
        }
    }

    // 다음 입력 화면으로 이동하는 메서드
    fun moveToUserVerification(){
        fragmentRegisterStep1Binding.apply {
            // 사용자가 입력한 데이터를 가져온다.
            val userId = registerStep1ViewModel?.textFieldRegisterStep1IdEditTextText?.value!!
            val userPw = registerStep1ViewModel?.textFieldRegisterStep1Pw1EditTextText?.value!!
            val userName = registerStep1ViewModel?.textFieldRegisterStep1FragmentNameEditTextText?.value!!
            // Log.d("test100", userId)
            // Log.d("test100", userPw)
            // 데이터를 담는다.
            val dataBundle = Bundle()
            dataBundle.putString("userId", userId)
            dataBundle.putString("userPw", userPw)
            dataBundle.putString("userName", userName)
            loginActivity.replaceFragment(FragmentName.REGISTER_VERIFICATION_FRAGMENT, true, true, dataBundle)
        }
    }


    // 이름, 아이디 중복 처리 메서드
    fun checkRegisterIdName() {
        // 사용자가 입력한 아이디
        val userId = fragmentRegisterStep1Binding.registerStep1ViewModel?.textFieldRegisterStep1IdEditTextText?.value!!
        // 사용자가 입력한 이름
        val userName = fragmentRegisterStep1Binding.registerStep1ViewModel?.textFieldRegisterStep1FragmentNameEditTextText?.value!!

        // 기존 에러 메시지 초기화
        fragmentRegisterStep1Binding.textFieldRegisterStep1Id.error = null
        fragmentRegisterStep1Binding.textFieldRegisterStep1FragmentName.error = null

        // 사용할 수 있는 아이디인지 검사
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                // 아이디 중복 체크
                val isIdAvailable = UserService.checkJoinUserId(userId)
                // 이름 중복 체크
                val isNameAvailable = UserService.checkJoinUserName(userName)

                Pair(isIdAvailable, isNameAvailable)
            }

            val (isIdAvailable, isNameAvailable) = work1.await()

            // 아이디 중복 여부 처리
            if (isIdAvailable) {
                // 사용할 수 있는 아이디일 때는 에러 메시지 제거
                fragmentRegisterStep1Binding.textFieldRegisterStep1Id.error = null
            } else {
                // 이미 존재하는 아이디일 때 에러 메시지 표시
                fragmentRegisterStep1Binding.textFieldRegisterStep1Id.error = "이미 존재하는 아이디입니다"
                // 소프트 키보드 표시
                loginActivity.showSoftInput(fragmentRegisterStep1Binding.textFieldRegisterStep1Id)
                // 2초 뒤에 에러 메시지 제거
                // clearErrorAfterDelay(fragmentRegisterStep1Binding.textFieldRegisterStep1Id, 2000)
            }

            // 이름 중복 여부 처리
            if (isNameAvailable) {
                // 사용할 수 있는 이름일 때는 에러 메시지 제거
                fragmentRegisterStep1Binding.textFieldRegisterStep1FragmentName.error = null
            } else {
                // 이미 존재하는 이름일 때 에러 메시지 표시
                fragmentRegisterStep1Binding.textFieldRegisterStep1FragmentName.error = "이미 존재하는 이름입니다"
                // 소프트 키보드 표시
                loginActivity.showSoftInput(fragmentRegisterStep1Binding.textFieldRegisterStep1FragmentName)
                // 2초 뒤에 에러 메시지 제거
                // clearErrorAfterDelay(fragmentRegisterStep1Binding.textFieldRegisterStep1FragmentName, 2000)
            }

            // 아이디와 이름이 모두 사용 가능한 경우에만 다음 화면으로 이동
            if (isIdAvailable && isNameAvailable) {
                moveToUserVerification()
            }
        }
    }

//    // 에러 메시지 제거 메서드
//    fun clearErrorAfterDelay(textField: TextInputLayout, delayMillis: Long) {
//        Handler().postDelayed({
//            textField.error = null
//        }, delayMillis)
//    }
}