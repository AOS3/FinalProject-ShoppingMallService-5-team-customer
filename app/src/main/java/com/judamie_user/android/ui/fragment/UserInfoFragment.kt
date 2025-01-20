package com.judamie_user.android.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentUserInfoBinding
import com.judamie_user.android.viewmodel.fragmentviewmodel.UserInfoViewModel

class UserInfoFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentUserInfoBinding: FragmentUserInfoBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentUserInfoBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_info,container,false)
        fragmentUserInfoBinding.userInfoViewModel = UserInfoViewModel(this)
        fragmentUserInfoBinding.lifecycleOwner = this

        //툴바설정
        settingMaterialToolbarWishList()


        return fragmentUserInfoBinding.root
    }

    //툴바설정
    private fun settingMaterialToolbarWishList() {
        fragmentUserInfoBinding.apply {
            //메뉴의 항목을 눌렀을때
            materialToolbarUserInfo.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuItemCartInUserInfoToolbar -> {
                        Log.d("test", "장바구니로 가는 함수")
                        // 장바구니로 가는 함수
                    }
                }
                true
            }
        }
    }

    // 설정화면이동
    fun goUserSettingFragment() {
        mainFragment.replaceFragment(ShopSubFragmentName.USER_SETTING_FRAGMENT, true, true, null)

    }

    // 주문내역이동
    fun goShowUserOrderListFragment() {
        mainFragment.replaceFragment(ShopSubFragmentName.SHOW_USER_ORDER_LIST_FRAGMENT,true,true,null)
    }

    // 쿠폰리스트이동
    fun goShowUserCouponListFragment(){
        mainFragment.replaceFragment(ShopSubFragmentName.SHOW_USER_COUPON_LIST_FRAGMENT,true,true,null)
    }

    // 주다미 소개 & 이용방법 화면 가기
    fun goShowAppInfoFragment(){
        mainFragment.replaceFragment(ShopSubFragmentName.SHOW_APP_INFO_FRAGMENT,true,true,null)
    }

    //개인정보 처리방침
    fun goShowAppPrivacyPolicyFragment(){
        mainFragment.replaceFragment(ShopSubFragmentName.SHOW_APP_PRIVACY_POLICY_FRAGMENT,true,true,null)
    }

    //서비스이용약관
    fun goShowAppTOSFragment(){
        mainFragment.replaceFragment(ShopSubFragmentName.SHOW_APP_TOS_FRAGMENT,true,true,null)
    }

}