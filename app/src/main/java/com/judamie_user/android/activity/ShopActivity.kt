package com.judamie_user.android.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.transition.MaterialSharedAxis
import com.judamie_user.android.R
import com.judamie_user.android.databinding.ActivityShopBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.temp.WriteProductReviewFragment

import com.judamie_user.android.viewmodel.activityviewmodel.ShopViewModel

class ShopActivity : AppCompatActivity() {

    lateinit var activityShopBinding: ActivityShopBinding

    // 현재 Fragment와 다음 Fragment를 담을 변수
    var newFragment: Fragment? = null
    var oldFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        activityShopBinding =
            DataBindingUtil.setContentView(this@ShopActivity, R.layout.activity_shop)
        activityShopBinding.lifecycleOwner = this@ShopActivity
        val shopViewModel = ViewModelProvider(this)[ShopViewModel::class.java]
        activityShopBinding.shopViewModel = shopViewModel

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        replaceFragment(ShopFragmentName.write, false, false, null)
    }

    // 프래그먼트를 교체하는 함수
    fun replaceFragment(
        fragmentName: ShopFragmentName,
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
            // 메인 화면 (상품 목록 화면)
            ShopFragmentName.MAIN_FRAGMENT -> MainFragment()
            ShopFragmentName.write -> WriteProductReviewFragment(MainFragment())
        }

        // bundle 객체가 null이 아니라면
        if (dataBundle != null) {
            newFragment?.arguments = dataBundle
        }

        // 프래그먼트 교체
        supportFragmentManager.commit {

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

            replace(R.id.fragmentContainerShop, newFragment!!)
            if (isAddToBackStack) {
                addToBackStack(fragmentName.str)
            }
        }
    }

    // 프래그먼트를 BackStack에서 제거하는 메서드
    fun removeFragment(fragmentName: ShopFragmentName) {
        supportFragmentManager.popBackStack(
            fragmentName.str,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

}

// 프래그먼트들을 나타내는 값들
enum class ShopFragmentName(var number: Int, var str: String) {
    MAIN_FRAGMENT(1,"MainFragment"),
    write(2,"write")
}
