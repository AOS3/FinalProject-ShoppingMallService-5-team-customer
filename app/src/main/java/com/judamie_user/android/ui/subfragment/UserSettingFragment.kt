package com.judamie_user.android.ui.subfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.judamie_user.android.R

import com.judamie_user.android.databinding.FragmentUserSettingBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.viewmodel.fragmentviewmodel.UserSettingViewModel


class UserSettingFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentUserSettingBinding: FragmentUserSettingBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentUserSettingBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_setting,container,false)
        fragmentUserSettingBinding.userSettingViewModel = UserSettingViewModel(this@UserSettingFragment)
        fragmentUserSettingBinding.lifecycleOwner = this


        return fragmentUserSettingBinding.root
    }

    fun movePrevFragment() {
        mainFragment.removeFragment(ShopSubFragmentName.USER_SETTING_FRAGMENT)
    }

    fun goModifyUserInfoFragment(){
        mainFragment.replaceFragment(ShopSubFragmentName.MODIFY_USER_INFO_FRAGMENT,true,true,null)
    }

    fun goSettingUserNotificationFragment() {
        mainFragment.replaceFragment(ShopSubFragmentName.SETTING_USER_NOTIFICATION_FRAGMENT,true,true,null)
    }

    fun showDialogLogout() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_logout, null)

        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancle)
        val buttonLogout = dialogView.findViewById<Button>(R.id.buttonLogout)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        //취소
        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        //로그아웃 처리
        buttonLogout.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showDialogMembershipWithdrawal() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_membership_withdrawal, null)

        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancle)
        val buttonMembershipWithdrawal = dialogView.findViewById<Button>(R.id.buttonMembershipWithdrawal)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        //취소
        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        //회원탈퇴 처리
        buttonMembershipWithdrawal.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


}