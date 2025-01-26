package com.judamie_user.android.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
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
import com.judamie_user.android.firebase.service.PickupLocationService
import com.judamie_user.android.firebase.service.ReviewService
import com.judamie_user.android.firebase.service.UserService
import com.judamie_user.android.viewmodel.fragmentviewmodel.HomeViewModel
import com.judamie_user.android.viewmodel.fragmentviewmodel.RegisterVerificationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


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
        // 탭 레이아웃 동작 메서드 호출
        showCategory()

        return fragmentHomeBinding.root
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar() {
        fragmentHomeBinding.apply {
            toolbarHome.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuItemHomeNotification -> {

                    }

                    R.id.menuItemHomeShoppingCart -> {
                        mainFragment.replaceFragment(
                            ShopSubFragmentName.SHOP_CART_FRAGMENT,
                            true,
                            true,
                            null
                        )
                    }
                }
                true
            }
        }
    }

    // 검색 화면으로 이동 메서드
    fun moveToSearch() {
        fragmentHomeBinding.apply {
            mainFragment.replaceFragment(ShopSubFragmentName.SEARCH_FRAGMENT, true, true, null)
        }
    }

    // 탭 레이아웃 동작 메서드
    fun showCategory() {
        fragmentHomeBinding.apply {
//            tabLayoutHome.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//                override fun onTabSelected(tab: TabLayout.Tab?) {
//                    // position : 사용자가 누른 탭의 순서 값
//
//                }
//                override fun onTabUnselected(tab: TabLayout.Tab?) {
//                    TODO("Not yet implemented")
//                }
//
//                override fun onTabReselected(tab: TabLayout.Tab?) {
//                    TODO("Not yet implemented")
//                }
//            })

            // ViewPager2 Adapter 설정
            pagerHome.adapter = ViewPagerAdapter(childFragmentManager, lifecycle, mainFragment)

            // TabLayout <-> ViewPager2 상호작용 설정
            TabLayoutMediator(tabLayoutHome, pagerHome) { tab, position ->
                val customView = LayoutInflater.from(requireContext()).inflate(R.layout.customtab_home, null)
                val iconView = customView.findViewById<ImageView>(R.id.imageViewTabIcon)
                val textView = customView.findViewById<TextView>(R.id.textViewTabText)

                // 텍스트와 아이콘 설정
                textView.text = (pagerHome.adapter as ViewPagerAdapter).tabTitles[position]
                // 탭 별 아이콘 설정
                when(position) {
                    0 -> {
                        iconView.setImageResource(R.drawable.categoryall)
                    }
                    1 -> {
                        iconView.setImageResource(R.drawable.winebottle)
                    }
                    2 -> {
                        iconView.setImageResource(R.drawable.whiskey)
                    }
                    3 -> {
                        iconView.setImageResource(R.drawable.vodka)
                    }
                    4 -> {
                        iconView.setImageResource(R.drawable.tequila)
                    }
                    5 -> {
                        iconView.setImageResource(R.drawable.soju)
                    }
                    6 -> {
                        iconView.setImageResource(R.drawable.sake)
                    }
                    7 -> {
                        iconView.setImageResource(R.drawable.rum)
                    }
                    8 -> {
                        iconView.setImageResource(R.drawable.liqueur)
                    }
                    9 -> {
                        iconView.setImageResource(R.drawable.chinabottle)
                    }
                    10 -> {
                        iconView.setImageResource(R.drawable.brandy)
                    }
                    11 -> {
                        iconView.setImageResource(R.drawable.beer)
                    }
                    12 -> {
                        iconView.setImageResource(R.drawable.nonalcoholic)
                    }


                    else -> null
                }
                tab.customView = customView
            }.attach()
        }
    }

    fun goSetPickupLocationFragment() {
        mainFragment.replaceFragment(ShopSubFragmentName.SET_PICKUP_LOCATION_FRAGMENT,true,true,null)
    }

    // ViewPager2의 Adapter
    inner class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, val mainFragment: MainFragment) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        val tabTitles = listOf(
            "전체", "와인", "위스키", "보드카", "데낄라", "우리술", "사케", "럼", "리큐르", "중국술",
            "브랜디", "맥주", "논알콜"
        )

        override fun getItemCount(): Int {
            return tabTitles.size
        }

        override fun createFragment(position: Int): Fragment {
            return ViewPagerFragment(mainFragment).apply {
                arguments = Bundle().apply {
                    putInt("TAB_INDEX", position)
                    putString("TAB_TITLE", tabTitles[position])
                }
            }
        }
    }
}