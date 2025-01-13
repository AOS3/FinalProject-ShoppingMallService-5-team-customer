package com.judamie_user.android.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.judamie_user.android.R
import com.judamie_user.android.activity.FragmentName
import com.judamie_user.android.activity.LoginActivity
import com.judamie_user.android.databinding.FragmentLoginBinding
import com.judamie_user.android.databinding.FragmentRegisterStep1Binding
import com.judamie_user.android.viewmodel.fragmentviewmodel.LoginViewModel
import com.judamie_user.android.viewmodel.fragmentviewmodel.RegisterStep1ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RegisterStep1Fragment : Fragment() {

    lateinit var fragmentRegisterStep1Binding: FragmentRegisterStep1Binding
    lateinit var loginActivity: LoginActivity



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            // Log.d("test100", userId)
            // Log.d("test100", userPw)
            // 데이터를 담는다.
            val dataBundle = Bundle()
            dataBundle.putString("userId", userId)
            dataBundle.putString("userPw", userPw)
            loginActivity.replaceFragment(FragmentName.REGISTER_VERIFICATION_FRAGMENT, true, true, dataBundle)
        }
    }

    // 이름, 아이디 중복 처리 메서드
    fun checkRegisterIdName() {

    }

    // 필드 입력 여부 처리 메서드

}