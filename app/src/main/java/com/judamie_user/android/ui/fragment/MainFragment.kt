package com.judamie_user.android.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.transition.MaterialSharedAxis
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentMainBinding
import com.judamie_user.android.ui.component.ShowPickupLocationDialogFragment
import com.judamie_user.android.ui.subfragment.ModifyUserInfoFragment
import com.judamie_user.android.ui.subfragment.PaymentProductFragment
import com.judamie_user.android.ui.subfragment.ProductInfoFragment
import com.judamie_user.android.ui.subfragment.ProductReviewListFragment
import com.judamie_user.android.ui.subfragment.SetPickUpLocationFragment
import com.judamie_user.android.ui.subfragment.SettingUserNotificationFragment
import com.judamie_user.android.ui.subfragment.ShowAppInfoFragment
import com.judamie_user.android.ui.subfragment.ShowAppPrivacyPolicyFragment
import com.judamie_user.android.ui.subfragment.ShowAppTOSFragment
import com.judamie_user.android.ui.subfragment.ShowReviewPhotoFragment
import com.judamie_user.android.ui.subfragment.ShowUserCouponListFragment
import com.judamie_user.android.ui.subfragment.ShowUserOrderInfoFragment
import com.judamie_user.android.ui.subfragment.ShowUserOrderListFragment
import com.judamie_user.android.ui.subfragment.UserNotificationListFragment
import com.judamie_user.android.ui.subfragment.UserProductReviewListFragment
import com.judamie_user.android.ui.subfragment.UserSettingFragment
import com.judamie_user.android.ui.subfragment.WriteProductReviewFragment
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
                    navigateWithoutBackStack(HomeFragment(this))
                    true
                }
                R.id.SEARCH_PRODUCT_FRAGMENT -> {
                    navigateWithBackStack(SearchFragment(this), "SearchFragment")
                    fragmentMainBinding.bottomNavigationView.visibility = View.GONE
                    false
                }
                R.id.WISH_LIST_FRAGMENT -> {
                    navigateWithoutBackStack(WishListFragment(this))
                    true
                }
                R.id.USER_INFO_FRAGMENT -> {
                    navigateWithoutBackStack(UserInfoFragment(this))
                    true
                }
                else -> false
            }
        }
    }

    // 백스택 없이 프래그먼트 교체
    private fun navigateWithoutBackStack(fragment: Fragment) {
        requireActivity().supportFragmentManager.commit {
            replace(R.id.mainFragmentContainerView, fragment)
        }
    }

    // 백스택에 추가하면서 프래그먼트 교체
    private fun navigateWithBackStack(fragment: Fragment, tag: String) {
        requireActivity().supportFragmentManager.commit {
            replace(R.id.mainFragmentContainerView, fragment)
            addToBackStack(tag) // 백스택에 추가
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
            ShopSubFragmentName.HOME_FRAGMENT -> HomeFragment(this)
            ShopSubFragmentName.SEARCH_FRAGMENT -> SearchFragment(this)
            ShopSubFragmentName.WISH_LIST_FRAGMENT -> WishListFragment(this)
            ShopSubFragmentName.USER_INFO_FRAGMENT -> UserInfoFragment(this)
            ShopSubFragmentName.MODIFY_USER_INFO_FRAGMENT -> ModifyUserInfoFragment(this)
            ShopSubFragmentName.SHOW_USER_ORDER_LIST_FRAGMENT -> ShowUserOrderListFragment(this)
            ShopSubFragmentName.SETTING_USER_NOTIFICATION_FRAGMENT -> SettingUserNotificationFragment(this)
            ShopSubFragmentName.USER_SETTING_FRAGMENT -> UserSettingFragment(this)
            ShopSubFragmentName.SHOP_CART_FRAGMENT -> ShopCartFragment(this)
            ShopSubFragmentName.PAYMENT_PRODUCT_FRAGMENT -> PaymentProductFragment(this)
            ShopSubFragmentName.SHOW_USER_COUPON_LIST_FRAGMENT -> ShowUserCouponListFragment(this)
            ShopSubFragmentName.SHOW_USER_ORDER_INFO_FRAGMENT -> ShowUserOrderInfoFragment(this)
            ShopSubFragmentName.WRITE_PRODUCT_REVIEW_FRAGMENT -> WriteProductReviewFragment(this)
            ShopSubFragmentName.SHOW_APP_INFO_FRAGMENT -> ShowAppInfoFragment(this)
            ShopSubFragmentName.SHOW_APP_PRIVACY_POLICY_FRAGMENT -> ShowAppPrivacyPolicyFragment(this)
            ShopSubFragmentName.SHOW_APP_TOS_FRAGMENT -> ShowAppTOSFragment(this)
            ShopSubFragmentName.USER_NOTIFICATION_LIST_FRAGMENT -> UserNotificationListFragment(this)
            ShopSubFragmentName.SET_PICKUP_LOCATION_FRAGMENT -> SetPickUpLocationFragment(this)
            ShopSubFragmentName.SHOW_PICKUP_LOCATION_DIALOG_FRAGMENT -> ShowPickupLocationDialogFragment(this)
            ShopSubFragmentName.PRODUCT_INFO_FRAGMENT -> ProductInfoFragment(this)
            ShopSubFragmentName.PRODUCT_REVIEW_LIST_FRAGMENT -> ProductReviewListFragment(this)
            ShopSubFragmentName.SHOW_REVIEW_PHOTO_FRAGMENT -> ShowReviewPhotoFragment(this)
            ShopSubFragmentName.VIEW_PAGER_FRAGMENT -> ViewPagerFragment(this)
            ShopSubFragmentName.USER_PRODUCT_REVIEW_FRAGMENT -> UserProductReviewListFragment(this)
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
    // 메인 화면 (홈)
    HOME_FRAGMENT(1, "HomeFragment"),
    // 검색 화면
    SEARCH_FRAGMENT(2, "SearchFragment"),
    // 찜 목록 화면
    WISH_LIST_FRAGMENT(3, "WishListFragment"),
    // 내 정보 화면
    USER_INFO_FRAGMENT(4, "UserInfoFragment"),
    // 내 정보 수정 화면
    MODIFY_USER_INFO_FRAGMENT(5, "ModifyUserInfoFragment"),
    // 주문 내역 확인 화면
    SHOW_USER_ORDER_LIST_FRAGMENT(6, "ShowUserOrderListFragment"),
    // 알림 설정 화면
    SETTING_USER_NOTIFICATION_FRAGMENT(7, "SettingUserNotificationFragment"),
    // 사용자 설정 화면
    USER_SETTING_FRAGMENT(8, "UserSettingFragment"),
    // 장바구니 화면
    SHOP_CART_FRAGMENT(9, "ShopCartFragment"),
    // 결제하기 화면
    PAYMENT_PRODUCT_FRAGMENT(10, "PaymentProductFragment"),
    // 제품 상세 페이지 화면
    PRODUCT_INFO_FRAGMENT(11, "ProductInfoFragment"),
    // 제품 리뷰 목록 화면
    PRODUCT_REVIEW_LIST_FRAGMENT(12, "ProductReviewListFragment"),
    // 리뷰 사진 크게보기 화면
    SHOW_REVIEW_PHOTO_FRAGMENT(13,"ShowReviewPhotoFragment"),


    // 주문상세 ShowUserOrderInfoFragment
    SHOW_USER_ORDER_INFO_FRAGMENT(14,"ShowUserOrderInfoFragment"),
    // 리뷰작성 WriteProductReviewFragment
    WRITE_PRODUCT_REVIEW_FRAGMENT(15,"WriteProductReviewFragment"),
    // 쿠폰보기 화면
    SHOW_USER_COUPON_LIST_FRAGMENT(16,"ShowUserCouponListFragment"),
    // 앱정보 보기 ShowAppInfoFragment
    SHOW_APP_INFO_FRAGMENT(17,"ShowAppInfoFragment"),
    // 개인정보 처리방침 ShowAppPrivacyPolicyFragment
    SHOW_APP_PRIVACY_POLICY_FRAGMENT(18,"ShowAppPrivacyPolicyFragment"),
    // 서비스이용약관 ShowAppTOSFragment
    SHOW_APP_TOS_FRAGMENT(19,"ShowAppTOSFragment"),
    // 알림창 UserNotificationListFragment
    USER_NOTIFICATION_LIST_FRAGMENT(20,"UserNotificationListFragment"),
    // 지도에서 픽업지 선택 SetPickUpLocationFragment
    SET_PICKUP_LOCATION_FRAGMENT(21,"SetPickUpLocationFragment"),
    // 픽업지 다이얼로그 ShowPickupLocationDialogFragment
    SHOW_PICKUP_LOCATION_DIALOG_FRAGMENT(22,"ShowPickupLocationDialogFragment"),
    // Home 화면 상품 목록 (ViewPager)
    VIEW_PAGER_FRAGMENT(23, "ViewPagerFragment"),
    // 사용자 리뷰 목록 화면
    USER_PRODUCT_REVIEW_FRAGMENT(24, "UserProductReviewFragment")


}