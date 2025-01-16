package com.judamie_user.android.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.judamie_user.android.R
import com.judamie_user.android.activity.LoginActivity
import com.judamie_user.android.activity.ShopActivity
import com.judamie_user.android.databinding.FragmentHomeBinding
import com.judamie_user.android.viewmodel.fragmentviewmodel.HomeViewModel
import com.judamie_user.android.viewmodel.fragmentviewmodel.RegisterVerificationViewModel


class HomeFragment(val mainFragment:MainFragment) : Fragment() {

    lateinit var fragmentHomeBinding: FragmentHomeBinding
    lateinit var shopActivity: ShopActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_home,
                container,
                false
            )
        fragmentHomeBinding.homeViewModel =
            HomeViewModel(this@HomeFragment)
        fragmentHomeBinding.lifecycleOwner = this@HomeFragment

        shopActivity = activity as ShopActivity

        // 툴바 구성 메서드 호출
        settingToolbar()


        return fragmentHomeBinding.root
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        fragmentHomeBinding.apply {
            toolbarHome.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menuItemHomeNotification -> {

                    }
                    R.id.menuItemHomeShoppingCart -> {
                        mainFragment.replaceFragment(ShopSubFragmentName.SHOP_CART_FRAGMENT, true, true, null)
                    }
                }
                true
            }
        }
    }

    // 검색 화면으로 이동 메서드
    fun moveToSearch(){
        fragmentHomeBinding.apply {
            mainFragment.replaceFragment(ShopSubFragmentName.SEARCH_FRAGMENT, true, true, null)
        }
    }

    // 탭 레이아웃 동작 메서드
    fun showCategory() {
        fragmentHomeBinding.apply {
            tabLayoutHome.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    // position : 사용자가 누른 탭의 순서 값

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    TODO("Not yet implemented")
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    TODO("Not yet implemented")
                }
            })

            // ViewPager2 Adapter 설정
            pagerHome.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)

            // TabLayout <-> ViewPager2 상호 작용을 위한 연동
            val tabLayoutMediator = TabLayoutMediator(tabLayoutHome, pagerHome) { tab, position ->
                // position 별로 분기하여 처리

            }
            tabLayoutMediator.attach()
        }
    }

    // ViewPager2의 Adapter
    inner class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
        // ViewPager2 - Fragment 개수
        override fun getItemCount(): Int {
            return 12
        }

        // position 번째 사용할 Fragment 객체를 생성하여 반환
        override fun createFragment(position: Int): Fragment {
            val newFragment = when (position) {
                0 -> ViewPagerFragment()
                1 -> ViewPagerFragment()
                2 -> ViewPagerFragment()
                3 -> ViewPagerFragment()
                4 -> ViewPagerFragment()
                5 -> ViewPagerFragment()
                6 -> ViewPagerFragment()
                7 -> ViewPagerFragment()
                8 -> ViewPagerFragment()
                9 -> ViewPagerFragment()
                10 -> ViewPagerFragment()
                11 -> ViewPagerFragment()
                else -> ViewPagerFragment()
            }
            return newFragment
        }

    }


}