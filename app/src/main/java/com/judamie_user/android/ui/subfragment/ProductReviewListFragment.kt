package com.judamie_user.android.ui.subfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.judamie_user.android.R
import com.judamie_user.android.activity.ShopActivity
import com.judamie_user.android.databinding.FragmentProductInfoBinding
import com.judamie_user.android.databinding.FragmentProductReviewListBinding
import com.judamie_user.android.databinding.RowProductIntoImgListBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.viewmodel.fragmentviewmodel.MainFragmentViewModel
import com.judamie_user.android.viewmodel.fragmentviewmodel.ProductReviewListViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowProductInfoImgListViewModel

class ProductReviewListFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentProductReviewListBinding: FragmentProductReviewListBinding
    lateinit var shopActivity: ShopActivity

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

//    // 상품 상세 이미지 RecyclerView 구성 메서드
//    fun settingCartRecyclerView(){
//        fragmentProductInfoBinding.apply {
//            recyclerViewProductInfoImg.adapter = ProductImgRecyclerViewAdapter()
//            recyclerViewProductInfoImg.layoutManager = LinearLayoutManager(shopActivity)
//
//            // 구분선 추가 X
//            // val deco = MaterialDividerItemDecoration(shopActivity, MaterialDividerItemDecoration.VERTICAL)
//            // recyclerViewProductInfoImg.addItemDecoration(deco)
//        }
//    }
//
//    // 상품 상세 이미지 RecyclerView 어답터
//    inner class ProductImgRecyclerViewAdapter : RecyclerView.Adapter<ProductImgRecyclerViewAdapter.ProductImgViewHolder>(){
//        inner class ProductImgViewHolder(val rowProductInfoImgListBinding: RowProductIntoImgListBinding) : RecyclerView.ViewHolder(rowProductInfoImgListBinding.root)
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImgViewHolder {
//            val rowProductInfoImgListBinding = DataBindingUtil.inflate<RowProductIntoImgListBinding>(layoutInflater, R.layout.row_product_into_img_list, parent, false)
//            rowProductInfoImgListBinding.rowProductInfoImgListViewModel = RowProductInfoImgListViewModel(this@ProductInfoFragment)
//            rowProductInfoImgListBinding.lifecycleOwner = this@ProductInfoFragment
//
//            val productImgViewHolder = ProductImgViewHolder(rowProductInfoImgListBinding)
//
//            return productImgViewHolder
//        }
//
//        override fun getItemCount(): Int {
//            return tempImgList1.size
//        }
//
//        override fun onBindViewHolder(holder: ProductImgViewHolder, position: Int) {
//            holder.rowProductInfoImgListBinding.rowImageViewProductInfoImg.setImageResource(tempImgList1[position])
//        }
//    }

}