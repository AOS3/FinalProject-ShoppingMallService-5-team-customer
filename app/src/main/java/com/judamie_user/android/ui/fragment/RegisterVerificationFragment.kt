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
import com.judamie_user.android.databinding.FragmentRegisterStep1Binding
import com.judamie_user.android.databinding.FragmentRegisterVerificationBinding
import com.judamie_user.android.viewmodel.fragmentviewmodel.RegisterStep1ViewModel
import com.judamie_user.android.viewmodel.fragmentviewmodel.RegisterVerificationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class RegisterVerificationFragment : Fragment() {

    lateinit var fragmentRegisterVerificationBinding: FragmentRegisterVerificationBinding
    lateinit var loginActivity: LoginActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentRegisterVerificationBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_register_verification,
                container,
                false
            )
        fragmentRegisterVerificationBinding.registerVerificationViewModel =
            RegisterVerificationViewModel(this@RegisterVerificationFragment)
        fragmentRegisterVerificationBinding.lifecycleOwner = this@RegisterVerificationFragment

        loginActivity = activity as LoginActivity

        // 툴바 구성 메서드 호출
        settingToolbar()

        return fragmentRegisterVerificationBinding.root

    }

    // 툴바 구성 메서드
    fun settingToolbar(){
        fragmentRegisterVerificationBinding.registerVerificationViewModel?.apply {
            // 타이틀
            toolbarRegisterVerificationTitle.value = "본인 인증"
            // 네비게이션 아이콘
            toolbarRegisterVerificationNavigationIcon.value = R.drawable.arrow_back_ios_24px
        }
    }

    // 가입 완료 처리 메서드
    fun proUserJoin(){
        fragmentRegisterVerificationBinding.apply {
            loginActivity.removeFragment(FragmentName.LOGIN_FRAGMENT)
        }
    }

}