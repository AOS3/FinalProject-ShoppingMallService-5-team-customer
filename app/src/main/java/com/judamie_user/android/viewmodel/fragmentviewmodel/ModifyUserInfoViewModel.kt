package com.judamie_user.android.viewmodel.fragmentviewmodel

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.judamie_user.android.ui.subfragment.ModifyUserInfoFragment

class ModifyUserInfoViewModel(val modifyUserInfoFragment: ModifyUserInfoFragment):ViewModel() {
    // materialToolbarModifyUserInfo
    companion object {
        // materialToolbarModifyUserInfo - onNavigationClickModifyUserInfo
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

    // textInputLayoutNewPassword1
    val textInputLayoutNewPassword1Text = MutableLiveData("")

    // textInputLayoutNewPassword2
    val textInputLayoutNewPassword2Text = MutableLiveData("")

    fun buttonSaveUserInfoOnClick() {
        val password1 = textInputLayoutNewPassword1Text.value ?: ""
        val password2 = textInputLayoutNewPassword2Text.value ?: ""

        when {
            password1.isEmpty() -> {
                modifyUserInfoFragment.fragmentModifyUserInfoBinding.textInputLayoutNewPassword1.error = "새 비밀번호를 입력해주세요."
            }
            password2.isEmpty() -> {
                modifyUserInfoFragment.fragmentModifyUserInfoBinding.textInputLayoutNewPassword2.error = "비밀번호 재입력을 입력해주세요."
            }
            password1.length < 4 -> {
                modifyUserInfoFragment.fragmentModifyUserInfoBinding.textInputLayoutNewPassword1.error = "비밀번호는 최소 4자 이상이어야 합니다."
            }
            password1 != password2 -> {
                modifyUserInfoFragment.fragmentModifyUserInfoBinding.textInputLayoutNewPassword2.error = "비밀번호가 일치하지 않습니다."
            }
            else -> {
                modifyUserInfoFragment.fragmentModifyUserInfoBinding.textInputLayoutNewPassword1.error = null
                modifyUserInfoFragment.fragmentModifyUserInfoBinding.textInputLayoutNewPassword2.error = null
                modifyUserInfoFragment.movePrevFragment()
            }
        }
    }




}