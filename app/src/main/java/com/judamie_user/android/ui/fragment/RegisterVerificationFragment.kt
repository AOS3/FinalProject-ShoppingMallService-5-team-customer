package com.judamie_user.android.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
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
import java.util.concurrent.TimeUnit


class RegisterVerificationFragment : Fragment() {

    lateinit var fragmentRegisterVerificationBinding: FragmentRegisterVerificationBinding
    lateinit var loginActivity: LoginActivity

    lateinit var auth: FirebaseAuth
    var verificationId: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentRegisterVerificationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_verification, container, false)
        fragmentRegisterVerificationBinding.registerVerificationViewModel =
            RegisterVerificationViewModel(this@RegisterVerificationFragment)
        fragmentRegisterVerificationBinding.lifecycleOwner = this@RegisterVerificationFragment

        loginActivity = activity as LoginActivity

        auth = FirebaseAuth.getInstance()

        // 툴바 구성 메서드 호출
        settingToolbar()

        return fragmentRegisterVerificationBinding.root

    }

    // 인증 코드 전송 메서드
    fun sendVerificationCode(phoneNumber: String) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(requireContext(), "인증 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                this@RegisterVerificationFragment.verificationId = verificationId
                Toast.makeText(requireContext(), "인증 코드가 전송되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // Callbacks
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // 인증 코드 확인 버튼
    fun verificationCheck() {
        val authCode = fragmentRegisterVerificationBinding.registerVerificationViewModel?.textFieldRegisterVerificationVerificationNoEditTextText?.value!!
        if (authCode.isNotEmpty()) {
            val credential = PhoneAuthProvider.getCredential(verificationId, authCode)
            signInWithPhoneAuthCredential(credential)
        } else {
            fragmentRegisterVerificationBinding.textFieldRegisterVerificationFragmentVerificationNo.error = "인증코드를 입력해주세요"
        }
    }

    // 인증 코드 확인 메서드
    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "인증 성공", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(requireContext(), "인증 실패", Toast.LENGTH_SHORT).show()
                }
            }
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
            loginActivity.removeFragment(FragmentName.REGISTER_STEP1_FRAGMENT)
        }
    }
}