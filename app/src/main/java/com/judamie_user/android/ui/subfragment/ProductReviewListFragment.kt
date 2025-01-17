package com.judamie_user.android.ui.subfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.judamie_user.android.R
import com.judamie_user.android.activity.ShopActivity
import com.judamie_user.android.databinding.FragmentProductReviewListBinding
import com.judamie_user.android.databinding.RowProductReviewAttachBinding
import com.judamie_user.android.databinding.RowProductReviewListBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.viewmodel.fragmentviewmodel.ProductReviewListViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowProductReviewListViewModel

class ProductReviewListFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentProductReviewListBinding: FragmentProductReviewListBinding
    lateinit var shopActivity: ShopActivity

    // ReyclerView 구성을 위한 임시 데이터
    val tempList1 = Array(20) {
        "술*술 님"
    }
    val tempImgList1 = Array(5) {
        R.drawable.sampleproductimage_gp18
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentProductReviewListBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_product_review_list,container,false)
        fragmentProductReviewListBinding.productReviewListViewModel = ProductReviewListViewModel(this)
        fragmentProductReviewListBinding.lifecycleOwner = this

        shopActivity = activity as ShopActivity



        // 툴바 구성 메서드 호출
        settingToolbar()
        // 상품 리뷰 RecyclerView 구성 메서드 호출
        settingProductReviewRecyclerView()

        return fragmentProductReviewListBinding.root
    }

    // 툴바 구성 메서드
    fun settingToolbar(){
        fragmentProductReviewListBinding.productReviewListViewModel?.apply {
            // 타이틀
            toolbarProductReviewTitle.value = "상품 리뷰"
            // 네비게이션 아이콘
            toolbarProductReviewNavigationIcon.value = R.drawable.arrow_back_ios_24px
        }
    }

    // 리뷰 쓰기 화면 이동 메서드
    fun moveToWriteReview() {
        // 상품 아이디 전달
    }

    // 상품 리뷰 목록 RecyclerView 구성 메서드
    fun settingProductReviewRecyclerView(){
        fragmentProductReviewListBinding.apply {
            recyclerViewProductReview.adapter = ProductReviewRecyclerViewAdapter()
            recyclerViewProductReview.layoutManager = LinearLayoutManager(shopActivity)
            val deco = MaterialDividerItemDecoration(shopActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewProductReview.addItemDecoration(deco)

        }
    }

    // 상품 리뷰 목록 RecyclerView 어답터
    inner class ProductReviewRecyclerViewAdapter : RecyclerView.Adapter<ProductReviewRecyclerViewAdapter.ProductReviewViewHolder>(){
        inner class ProductReviewViewHolder(val rowProductReviewListBinding: RowProductReviewListBinding) : RecyclerView.ViewHolder(rowProductReviewListBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductReviewViewHolder {
            val rowProductReviewListBinding = DataBindingUtil.inflate<RowProductReviewListBinding>(layoutInflater, R.layout.row_product_review_list, parent, false)
            rowProductReviewListBinding.rowProductReviewListViewModel = RowProductReviewListViewModel(this@ProductReviewListFragment)
            rowProductReviewListBinding.lifecycleOwner = this@ProductReviewListFragment

            val productReviewViewHolder = ProductReviewViewHolder(rowProductReviewListBinding)

            return productReviewViewHolder
        }

        override fun getItemCount(): Int {
            return tempList1.size
        }

        override fun onBindViewHolder(holder: ProductReviewViewHolder, position: Int) {
            holder.rowProductReviewListBinding.rowProductReviewListViewModel?.rowTextViewProductReviewNameText?.value = tempList1[position]
            // Nested RecyclerView 설정
            holder.rowProductReviewListBinding.rowRecyclerViewProductReviewAttach.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = ProductReviewAttachRecyclerViewAdapter() // 사진 데이터 전달
            }
        }
    }

    // 상품 리뷰 사진 목록 RecyclerView 어답터
    inner class ProductReviewAttachRecyclerViewAdapter() : RecyclerView.Adapter<ProductReviewAttachRecyclerViewAdapter.ProductReviewAttachViewHolder>(){
        inner class ProductReviewAttachViewHolder(val rowProductReviewAttachBinding: RowProductReviewAttachBinding) : RecyclerView.ViewHolder(rowProductReviewAttachBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductReviewAttachViewHolder {
            val binding = DataBindingUtil.inflate<RowProductReviewAttachBinding>(
                LayoutInflater.from(parent.context),
                R.layout.row_product_review_attach,
                parent,
                false
            )
            return ProductReviewAttachViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return tempImgList1.size
        }

        override fun onBindViewHolder(holder: ProductReviewAttachViewHolder, position: Int) {
            holder.rowProductReviewAttachBinding.rowImageViewProductReviewAttatch.setImageResource(tempImgList1[position])

        }
    }

}