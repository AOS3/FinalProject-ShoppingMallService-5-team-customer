package com.judamie_user.android.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.transition.MaterialSharedAxis
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentMainBinding
import com.judamie_user.android.ui.subfragment.ModifyUserInfoFragment
import com.judamie_user.android.ui.subfragment.SettingUserNotificationFragment
import com.judamie_user.android.ui.subfragment.ShowUserOrderListFragment
import com.judamie_user.android.ui.subfragment.UserSettingFragment
import com.judamie_user.android.viewmodel.fragmentviewmodel.MainFragmentViewModel


class MainFragment() : Fragment() {
    lateinit var fragmentMainBinding:FragmentMainBinding

    // 현재 Fragment와 다음 Fragment를 담을 변수(애니메이션 이동 때문에...)
    var newFragment: Fragment? = null
    var oldFragment: Fragment? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMainBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false)
        fragmentMainBinding.mainFragmentViewModel = MainFragmentViewModel(this)
        fragmentMainBinding.lifecycleOwner = this


        settingBottomNavigationView()

        // BackStack 변경 시 하단 네비게이션 visibility 설정
        requireActivity().supportFragmentManager.addOnBackStackChangedListener {
            if (requireActivity().supportFragmentManager.backStackEntryCount == 0) {
                fragmentMainBinding.bottomNavigationView.visibility = View.VISIBLE
            }
        }

        //코드 동작 설명 from gpt
        //kotlin
        //코드 복사
        //// BackStack 변경 시 하단 네비게이션 visibility 설정
        //requireActivity().supportFragmentManager.addOnBackStackChangedListener {
        //    if (requireActivity().supportFragmentManager.backStackEntryCount == 0) {
        //        fragmentMainBinding.bottomNavigationView.visibility = View.VISIBLE
        //    }
        //}
        //addOnBackStackChangedListener:
        //이 코드는 FragmentManager의 **BackStack(뒤로 가기 스택)**이 변경될 때 호출되는 리스너입니다. 즉, 프래그먼트를 교체하거나 뒤로 가기(Back)를 했을 때마다 호출됩니다.
        //
        //backStackEntryCount == 0 체크:
        //supportFragmentManager.backStackEntryCount는 현재 BackStack에 남아 있는 프래그먼트의 개수를 나타냅니다.
        //
        //만약 backStackEntryCount가 0이면, BackStack에 아무런 프래그먼트도 남아 있지 않다는 의미입니다.
        //즉, MainFragment가 화면에 다시 나타난 상태입니다.
        //따라서 하단 네비게이션이 필요한 MainFragment가 다시 보이게 되었으므로 하단 네비게이션을 VISIBLE로 설정합니다.
        //왜 필요한가?
        //replaceFragment 메서드에서 하단 네비게이션을 숨기도록 설정(View.GONE) 했기 때문에, enum 클래스에 정의된 하위 프래그먼트로 이동하면 하단 네비게이션이 보이지 않게 됩니다.
        //그러나 사용자가 뒤로 가기(Back)를 눌러 MainFragment로 돌아오면, 하단 네비게이션이 다시 보여야 하므로 VISIBLE로 설정해야 합니다.
        //이 동작을 자동으로 처리하기 위해 BackStack의 상태가 변경될 때 하단 네비게이션을 다시 보이도록 리스너를 설정한 것입니다.
        //동작 흐름 요약
        //MainFragment에서 하단 네비게이션을 관리하며, 특정 프래그먼트로 이동할 때는 replaceFragment에서 하단 네비게이션을 숨김(GONE).
        //뒤로 가기(Back)를 하면 BackStack에 남아 있는 프래그먼트가 제거되면서 MainFragment로 돌아오게 됨.
        //이때 BackStack 변경 리스너가 호출되고, backStackEntryCount == 0인 경우 하단 네비게이션을 다시 보이도록 설정.
        //정리
        //네, 이 부분이 바로 하단 네비게이션이 필요한 프래그먼트(MainFragment)로 돌아올 때 다시 하단 네비게이션을 보이게 해주는 역할을 합니다.
        //
        //혹시 더 궁금한 부분이 있으면 말씀해 주세요! 😊

        // 앱 초기 실행 시 홈화면으로 설정
        if (savedInstanceState == null) {
            fragmentMainBinding.bottomNavigationView.selectedItemId = R.id.HOME_FRAGMENT
        }


        return fragmentMainBinding.root
    }
    // 하단 네비게이션 설정
    private fun settingBottomNavigationView() {
        fragmentMainBinding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.HOME_FRAGMENT -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.mainFragmentContainerView, HomeFragment(this))
                        .commit()
                    true
                }

                R.id.SEARCH_PRODUCT_FRAGMENT -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.mainFragmentContainerView, SearchFragment(this))
                        .commit()
                    true
                }

                R.id.WISH_LIST_FRAGMENT -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.mainFragmentContainerView, WishListFragment(this))
                        .commit()
                    true
                }

                R.id.USER_INFO_FRAGMENT -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.mainFragmentContainerView, UserInfoFragment(this))
                        .commit()
                    true
                }

                else -> false
            }
        }
    }


    // 프래그먼트를 교체하는 함수
    fun replaceFragment(
        fragmentName: ShopSubFragmentName,
        isAddToBackStack: Boolean,
        animate: Boolean,
        dataBundle: Bundle?
    ) {
        // newFragment가 null이 아니라면 oldFragment 변수에 담아준다.
        if (newFragment != null) {
            oldFragment = newFragment
        }

        // 프래그먼트 객체
        newFragment = when (fragmentName) {
            ShopSubFragmentName.USER_SETTING_FRAGMENT -> UserSettingFragment(this)
            ShopSubFragmentName.MODIFY_USER_INFO_FRAGMENT -> ModifyUserInfoFragment(this)
            ShopSubFragmentName.SETTING_USER_NOTIFICATION_FRAGMENT -> SettingUserNotificationFragment(this)
            ShopSubFragmentName.SHOW_USER_ORDER_LIST_FRAGMENT -> ShowUserOrderListFragment(this)
        }

        // bundle 객체가 null이 아니라면
        if (dataBundle != null) {
            newFragment?.arguments = dataBundle
        }

        // **하단 네비게이션 숨기기**
        fragmentMainBinding.bottomNavigationView.visibility = View.GONE

        // 프래그먼트 교체
        requireActivity().supportFragmentManager.commit {

            if (animate) {
                // 만약 이전 프래그먼트가 있다면
                if (oldFragment != null) {
                    oldFragment?.exitTransition =
                        MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                    oldFragment?.reenterTransition =
                        MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
                }

                newFragment?.exitTransition =
                    MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                newFragment?.reenterTransition =
                    MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
                newFragment?.enterTransition =
                    MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                newFragment?.returnTransition =
                    MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
            }

            replace(R.id.mainFragmentContainerView, newFragment!!)
            if (isAddToBackStack) {
                addToBackStack(fragmentName.str)
            }
        }
    }

    // 프래그먼트를 BackStack에서 제거하는 메서드
    fun removeFragment(fragmentName: ShopSubFragmentName) {
        requireActivity().supportFragmentManager.popBackStack(
            fragmentName.str,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }
}



// 프래그먼트들을 나타내는 값들
enum class ShopSubFragmentName(var number: Int, var str: String) {

    //UserSettingFragment
    USER_SETTING_FRAGMENT(1,"UserSettingFragment"),

    //ModifyUserInfoFragment
    MODIFY_USER_INFO_FRAGMENT(2,"ModifyUserInfoFragment"),

    //SettingUserNotificationFragment
    SETTING_USER_NOTIFICATION_FRAGMENT(3,"SettingUserNotificationFragment"),

    //ShowUserOrderListFragment
    SHOW_USER_ORDER_LIST_FRAGMENT(4,"ShowUserOrderListFragment")

}
