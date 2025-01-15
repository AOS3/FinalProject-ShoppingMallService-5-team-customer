package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.activity.FragmentName
import com.judamie_user.android.ui.fragment.LoginFragment
import com.judamie_user.android.ui.fragment.RegisterStep1Fragment

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


    // buttonLoginNextStep - onClick
    fun buttonLoginNextStepOnClick(){
        registerStep1Fragment.moveToUserVerification()
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