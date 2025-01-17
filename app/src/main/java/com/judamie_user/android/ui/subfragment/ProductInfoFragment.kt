package com.judamie_user.android.ui.subfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.judamie_user.android.R
import com.judamie_user.android.activity.ShopActivity
import com.judamie_user.android.databinding.FragmentProductInfoBinding
import com.judamie_user.android.databinding.RowProductIntoImgListBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.viewmodel.fragmentviewmodel.ProductInfoViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowProductInfoImgListViewModel

class ProductInfoFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentProductInfoBinding: FragmentProductInfoBinding
    lateinit var shopActivity: ShopActivity

    // 상품 초기 수량
    var productCount:Int = 1

    // ReyclerView 구성을 위한 임시 데이터
    val tempImgList1 = Array(5) {
        R.drawable.sampleproductimage_gp18
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentProductInfoBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_info, container, false)
        fragmentProductInfoBinding.productInfoViewModel =
            ProductInfoViewModel(this@ProductInfoFragment)
        fragmentProductInfoBinding.lifecycleOwner = this@ProductInfoFragment

        shopActivity = activity as ShopActivity

        // 툴바 구성 메서드 호출
        settingToolbar()
        // 상품 이미지 RecyclerView 구성 메서드 호출
        settingCartRecyclerView()
        // RatingBar 속성 설정
        setupRatingBar()

        return fragmentProductInfoBinding.root
    }

    // 별점 설정 메서드
    fun setupRatingBar() {
        fragmentProductInfoBinding.apply {
            // 상품의 별점 정보를 가져온다.
            // 평균 별점 구한다.
            //
        }
    }

    // 리뷰 화면으로 이동하는 메서드
    fun moveToReviewList() {
        fragmentProductInfoBinding.apply {
            val dataBundle = Bundle()
            mainFragment.replaceFragment(ShopSubFragmentName.PRODUCT_REVIEW_LIST_FRAGMENT, true, true, dataBundle)
        }

    }

    // 화면 구성 메서드
    fun settingData(){

    }

    // 툴바 구성 메서드
    fun settingToolbar(){
        fragmentProductInfoBinding.productInfoViewModel?.apply {
            // 타이틀
            toolbarProductInfoTitle.value = "제품 상세"
            // 네비게이션 아이콘
            toolbarProductInfoNavigationIcon.value = R.drawable.arrow_back_ios_24px
        }
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        mainFragment.removeFragment(ShopSubFragmentName.PRODUCT_INFO_FRAGMENT)
    }

    // 상품 수량 선택 메서드
    fun selectProductCount(isIncrease:Boolean) {
        fragmentProductInfoBinding.apply {
            val currentCount = productInfoViewModel?.textViewProductInfoCntText?.value?.toIntOrNull() ?: 1

            if (isIncrease) {
                // 수량 증가
                productInfoViewModel?.textViewProductInfoCntText?.value = (currentCount + 1).toString()
            } else {
                // 수량 감소 (1 이하로는 감소하지 않음)
                if (currentCount > 1) {
                    productInfoViewModel?.textViewProductInfoCntText?.value = (currentCount - 1).toString()
                } else {
                    shopActivity.runOnUiThread {
                        Toast.makeText(
                            shopActivity,
                            "최소 수량입니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    // 바로 구매 메서드
    fun buyProduct() {
        // Intent 전달 (상품정보, 수량)
        val bundle = Bundle()

        // 결제 화면으로 이동
        mainFragment.replaceFragment(ShopSubFragmentName.PAYMENT_PRODUCT_FRAGMENT, true, true, bundle)
    }

    // 장바구니 넣기 메서드
    fun addCartProduct() {
        fragmentProductInfoBinding.apply {
            // 동일한 상품이 장바구니에 없을 경우
            // WishList 상품 추가O + 토스트 표시 : "장바구니에 상품이 추가되었습니다"
            Toast.makeText(shopActivity, "장바구니에 상품이 추가되었습니다", Toast.LENGTH_SHORT).show()
            // 동일한 상품이 장바구니에 있을 경우
            // WishList 상품 추가X + 토스트 표시 : "장바구니에 동일한 상품이 있습니다."
        }
    }

    // 상품 상세 이미지 RecyclerView 구성 메서드
    fun settingCartRecyclerView(){
        fragmentProductInfoBinding.apply {
            recyclerViewProductInfoImg.adapter = ProductImgRecyclerViewAdapter()
            recyclerViewProductInfoImg.layoutManager = LinearLayoutManager(shopActivity)

            // 구분선 추가 X
            // val deco = MaterialDividerItemDecoration(shopActivity, MaterialDividerItemDecoration.VERTICAL)
            // recyclerViewProductInfoImg.addItemDecoration(deco)
        }
    }

    // 상품 상세 이미지 RecyclerView 어답터
    inner class ProductImgRecyclerViewAdapter : RecyclerView.Adapter<ProductImgRecyclerViewAdapter.ProductImgViewHolder>(){
        inner class ProductImgViewHolder(val rowProductInfoImgListBinding: RowProductIntoImgListBinding) : RecyclerView.ViewHolder(rowProductInfoImgListBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImgViewHolder {
            val rowProductInfoImgListBinding = DataBindingUtil.inflate<RowProductIntoImgListBinding>(layoutInflater, R.layout.row_product_into_img_list, parent, false)
            rowProductInfoImgListBinding.rowProductInfoImgListViewModel = RowProductInfoImgListViewModel(this@ProductInfoFragment)
            rowProductInfoImgListBinding.lifecycleOwner = this@ProductInfoFragment

            val productImgViewHolder = ProductImgViewHolder(rowProductInfoImgListBinding)

            return productImgViewHolder
        }

        override fun getItemCount(): Int {
            return tempImgList1.size
        }

        override fun onBindViewHolder(holder: ProductImgViewHolder, position: Int) {
            holder.rowProductInfoImgListBinding.rowImageViewProductInfoImg.setImageResource(tempImgList1[position])
        }
    }
}