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
    fun proLogin(){
        fragmentLoginBinding.apply {
            loginActivity.finish()
        }
    }

    // 회원 가입 화면으로 이동하는 메서드
    fun moveToRegisterStep1() {
        loginActivity.replaceFragment(FragmentName.REGISTER_STEP1_FRAGMENT, true, true, null)
    }

}