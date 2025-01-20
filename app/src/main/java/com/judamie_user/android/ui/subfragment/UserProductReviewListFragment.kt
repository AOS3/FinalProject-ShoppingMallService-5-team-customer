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
import com.judamie_user.android.databinding.FragmentUserProductReviewListBinding
import com.judamie_user.android.databinding.RowProductReviewAttachBinding
import com.judamie_user.android.databinding.RowProductReviewListBinding
import com.judamie_user.android.databinding.RowUserProductReviewListBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.viewmodel.fragmentviewmodel.UserProductReviewListViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowProductReviewListViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowUserProductReviewListViewModel


class UserProductReviewListFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentUserProductReviewListBinding: FragmentUserProductReviewListBinding
    lateinit var shopActivity: ShopActivity

    // ReyclerView 구성을 위한 임시 데이터
    val tempList1 = Array(20) {
        "Royal Salute 21Yo Malts 500ml"
    }
    val tempImgList1 = Array(5) {
        R.drawable.sampleproductimage_gp18
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentUserProductReviewListBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_product_review_list,container,false)
        fragmentUserProductReviewListBinding.userProductReviewViewModel = UserProductReviewListViewModel(this)
        fragmentUserProductReviewListBinding.lifecycleOwner = viewLifecycleOwner

        shopActivity = activity as ShopActivity

        // 툴바 구성 메서드 호출
        settingToolbar()
        // 상품 리뷰 RecyclerView 구성 메서드 호출
        settingProductReviewRecyclerView()

        return fragmentUserProductReviewListBinding.root
    }

    // 툴바 구성 메서드
    fun settingToolbar(){
        fragmentUserProductReviewListBinding.userProductReviewViewModel?.apply {
            // 타이틀
            toolbarUserProductReviewTitle.value = "홍*동 님의 리뷰"
            // 네비게이션 아이콘
            toolbarUserProductReviewNavigationIcon.value = R.drawable.arrow_back_ios_24px
        }
    }

    // 상품 리뷰 목록 RecyclerView 구성 메서드
    fun settingProductReviewRecyclerView(){
        fragmentUserProductReviewListBinding.apply {
            recyclerViewUserProductReview.adapter = UserReviewRecyclerViewAdapter()
            recyclerViewUserProductReview.layoutManager = LinearLayoutManager(shopActivity)
            val deco = MaterialDividerItemDecoration(shopActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewUserProductReview.addItemDecoration(deco)

        }
    }

    // 상품 리뷰 목록 RecyclerView 어답터
    inner class UserReviewRecyclerViewAdapter : RecyclerView.Adapter<UserReviewRecyclerViewAdapter.UserReviewViewHolder>(){
        inner class UserReviewViewHolder(val rowUserProductReviewListBinding: RowUserProductReviewListBinding) : RecyclerView.ViewHolder(rowUserProductReviewListBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserReviewViewHolder {
            val rowUserReviewListBinding = DataBindingUtil.inflate<RowUserProductReviewListBinding>(layoutInflater, R.layout.row_user_product_review_list, parent, false)
            rowUserReviewListBinding.rowUserProductReviewListViewModel = RowUserProductReviewListViewModel(this@UserProductReviewListFragment)
            rowUserReviewListBinding.lifecycleOwner = this@UserProductReviewListFragment

            val userReviewViewHolder = UserReviewViewHolder(rowUserReviewListBinding)

            return userReviewViewHolder
        }

        override fun getItemCount(): Int {
            return tempList1.size
        }

        override fun onBindViewHolder(holder: UserReviewViewHolder, position: Int) {
            holder.rowUserProductReviewListBinding.rowUserProductReviewListViewModel?.rowTextViewUserProductReviewProductNameText?.value = tempList1[position]
            // Nested RecyclerView 설정
            holder.rowUserProductReviewListBinding.rowRecyclerViewUserProductReviewAttach.apply {
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