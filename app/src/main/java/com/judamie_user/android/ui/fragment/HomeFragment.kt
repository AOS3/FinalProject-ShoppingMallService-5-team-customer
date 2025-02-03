package com.judamie_user.android.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.judamie_user.android.R
import com.judamie_user.android.activity.ShopActivity
import com.judamie_user.android.databinding.FragmentHomeBinding
import com.judamie_user.android.firebase.service.PickupLocationService
import com.judamie_user.android.firebase.service.UserService
import com.judamie_user.android.viewmodel.fragmentviewmodel.HomeViewModel
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

        // 픽업지를 고르러갈 버튼을 구성한다
        settingButtonHomePickupLoc()


        return fragmentHomeBinding.root
    }
    // 픽업지를 고르러갈 버튼을 구성한다
    fun settingButtonHomePickupLoc(){
        CoroutineScope(Dispatchers.Main).launch {
            val userInfo = async(Dispatchers.IO){
                UserService.selectUserDataByUserDocumentIdOne(shopActivity.userDocumentID)
            }
            val userModel = userInfo.await()
            if (userModel.userPickupLoc.isEmpty()){
                fragmentHomeBinding.homeViewModel?.buttonHomePickupLocText?.value = "픽업지설정안됨"
                showUserMustSetPickupLocationDialog()
            }else{
                val userPickupLoc  = userModel.userPickupLoc
                CoroutineScope(Dispatchers.Main).launch {
                    val workPickupLocationModel = async(Dispatchers.IO) {
                        PickupLocationService.gettingPickupLocationById(userPickupLoc)
                    }
                    val pickupLocationModel = workPickupLocationModel.await()
                    fragmentHomeBinding.homeViewModel?.buttonHomePickupLocText?.value = pickupLocationModel.pickupLocName
                }
            }
        }
    }

    //픽업지가 비워져있다면 사용자에게 픽업지를 꼭 고르도록 강제한다
    fun showUserMustSetPickupLocationDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_user_must_set_pickup_location, null)
        val buttonUserMustSetPickupLocation = dialogView.findViewById<Button>(R.id.buttonUserMustSetPickupLocation)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        // 다이얼로그를 취소할 수 없게 설정
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        // 픽업지 설정 버튼 클릭 이벤트
        buttonUserMustSetPickupLocation.setOnClickListener {
            // 다이얼로그 닫기
            dialog.dismiss()

            // 픽업지 설정 프래그먼트로 이동
            mainFragment.replaceFragment(ShopSubFragmentName.SET_PICKUP_LOCATION_FRAGMENT, true, true, null)
        }

        dialog.show()
    }




    // 툴바를 구성하는 메서드
    fun settingToolbar() {
        fragmentHomeBinding.apply {
            toolbarHome.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuItemHomeNotification -> {
                        mainFragment.replaceFragment(
                            ShopSubFragmentName.USER_NOTIFICATION_LIST_FRAGMENT,
                            true,
                            true,
                            null
                        )
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

            // ViewPager2 Adapter 설정
            pagerHome.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)

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

            // Tab 클릭 이벤트 처리
            tabLayoutHome.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val position = tab?.position ?: 0
                    // Log.d("test100", "누른탭 : $position")
                    // 선택된 탭의 ViewPager로 이동
                    pagerHome.setCurrentItem(position, true)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    val position = tab?.position ?: 0
                    // Log.d("test100", "누른탭 : $position")
                    pagerHome.setCurrentItem(position, true)
                }

            })
        }
    }

    fun goSetPickupLocationFragment() {
        mainFragment.replaceFragment(ShopSubFragmentName.SET_PICKUP_LOCATION_FRAGMENT,true,true,null)
    }

    // ViewPager2의 Adapter
    inner class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        val tabTitles = listOf(
            "전체","와인", "위스키", "보드카", "데낄라", "우리술", "사케", "럼", "리큐르", "중국술",
            "브랜디", "맥주", "논알콜"
        )

        override fun getItemCount(): Int {
            return tabTitles.size
        }

        override fun createFragment(position: Int): Fragment {
            return ViewPagerFragment.newInstance(tabTitles[position], mainFragment)
        }
    }
}