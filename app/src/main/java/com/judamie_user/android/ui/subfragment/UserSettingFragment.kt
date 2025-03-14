package com.judamie_user.android.ui.subfragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.judamie_user.android.R
import com.judamie_user.android.activity.LoginActivity
import com.judamie_user.android.activity.ShopActivity

import com.judamie_user.android.databinding.FragmentUserSettingBinding
import com.judamie_user.android.firebase.service.UserService
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.util.UserState
import com.judamie_user.android.viewmodel.fragmentviewmodel.UserSettingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.log


class UserSettingFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentUserSettingBinding: FragmentUserSettingBinding
    lateinit var shopActivity: ShopActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentUserSettingBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_setting,container,false)
        fragmentUserSettingBinding.userSettingViewModel = UserSettingViewModel(this@UserSettingFragment)
        fragmentUserSettingBinding.lifecycleOwner = this
        shopActivity = activity as ShopActivity

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

        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancel)
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
            // 자동 로그인을 위한 토큰값이 있다면 삭제한다.
            val pref = shopActivity.getSharedPreferences("LoginToken",Context.MODE_PRIVATE)
            pref.edit {
                remove("token")
            }
            val loginActivity = Intent(shopActivity,LoginActivity::class.java)
            startActivity(loginActivity)
            shopActivity.finish()
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showDialogMembershipWithdrawal() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_membership_withdrawal, null)

        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancel)
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
            //탈퇴처리
            CoroutineScope(Dispatchers.Main).launch {
                val work = async(Dispatchers.IO){
                    UserService.updateUserState(shopActivity.userDocumentID, UserState.USER_STATE_SIGN_OUT)
                }
                work.join()
                // 자동 로그인을 위한 토큰값이 있다면 삭제한다.
                val pref = shopActivity.getSharedPreferences("LoginToken",Context.MODE_PRIVATE)
                pref.edit {
                    remove("token")
                }

                Toast.makeText(shopActivity,"주다미를 이용해주셔서 감사합니다",Toast.LENGTH_LONG).show()

                val loginActivity = Intent(shopActivity,LoginActivity::class.java)
                startActivity(loginActivity)
                shopActivity.finish()


                dialog.dismiss()
            }
        }

        dialog.show()
    }


}