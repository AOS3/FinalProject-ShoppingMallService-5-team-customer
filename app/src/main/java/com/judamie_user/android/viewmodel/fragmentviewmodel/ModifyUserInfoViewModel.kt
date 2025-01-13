package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.subfragment.ModifyUserInfoFragment

class ModifyUserInfoViewModel(val modifyUserInfoFragment: ModifyUserInfoFragment):ViewModel() {

    //materialToolbarModifyUserInfo
    companion object {
        //materialToolbarModifyUserInfo - onNavigationClickModifyUserInfo
        @JvmStatic
        @BindingAdapter("onNavigationClickModifyUserInfo")
        fun onNavigationClickModifyUserInfo(
            materialToolbar: MaterialToolbar,
            modifyUserInfoFragment: ModifyUserInfoFragment
        ) {
            materialToolbar.setNavigationOnClickListener {
                modifyUserInfoFragment.movePrevFragment()
            }
        }
    }

    //textInputLayoutPhoneNumber
    val textInputLayoutPhoneNumberText = MutableLiveData("")

    //buttonRequestCertification
    fun buttonRequestCertificationOnClick(){

    }

    //buttonChangePhoneNumber
    fun buttonChangePhoneNumberOnClick(){

    }

    //textInputLayoutCertificationNumber
    val textInputLayoutCertificationNumberText = MutableLiveData("")

    //textInputLayoutNewPassword1
    val textInputLayoutNewPassword1 = MutableLiveData("")

    //textInputLayoutNewPassword2
    val textInputLayoutNewPassword2 = MutableLiveData("")

    //buttonSaveUserInfo
    fun buttonSaveUserInfoOnClick(){
        modifyUserInfoFragment.movePrevFragment()
    }
}


//companion object {
//        //materialToolbarUserSetting - onNavigationClickUserSetting
//        @JvmStatic
//        @BindingAdapter("onNavigationClickUserSetting")
//        fun onNavigationClickUserSetting(
//            materialToolbar: MaterialToolbar,
//            userSettingFragment: UserSettingFragment
//        ) {
//            materialToolbar.setNavigationOnClickListener {
//                userSettingFragment.movePrevFragment()
//            }
//        }
//    }