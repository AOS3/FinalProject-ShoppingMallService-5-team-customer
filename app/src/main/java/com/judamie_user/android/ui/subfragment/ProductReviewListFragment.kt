package com.judamie_user.android.ui.subfragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.judamie_user.android.R
import com.judamie_user.android.activity.ShopActivity
import com.judamie_user.android.databinding.FragmentProductReviewListBinding
import com.judamie_user.android.databinding.RowProductReviewAttachBinding
import com.judamie_user.android.databinding.RowProductReviewListBinding
import com.judamie_user.android.firebase.model.ReviewModel
import com.judamie_user.android.firebase.service.ProductService
import com.judamie_user.android.firebase.service.ReviewService
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.viewmodel.fragmentviewmodel.ProductReviewListViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowProductReviewListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ProductReviewListFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentProductReviewListBinding: FragmentProductReviewListBinding
    lateinit var shopActivity: ShopActivity

    var adapter = ProductReviewRecyclerViewAdapter()

    // ReyclerView 구성을 위한 임시 데이터
    val tempList1 = Array(20) {
        "술*술 님"
    }
    val tempImgList1 = Array(5) {
        R.drawable.sampleproductimage_gp18
    }

    // 처음에 물건데이터에서 리뷰documentID를 뽑아서 저장하는 리스트
    var reviewsIdList = mutableListOf<String>()

    //reviewsIdList를 토대로 리뷰를 하나씩가져와서 모델리스트에담는다
    var reviewsModelList = mutableListOf<ReviewModel>()

    // 이미지 파일을 리뷰별로 분류한다
    var reviewImagesMap = mutableMapOf<String,MutableList<Uri>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentProductReviewListBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_product_review_list,container,false)
        fragmentProductReviewListBinding.productReviewListViewModel = ProductReviewListViewModel(this)
        fragmentProductReviewListBinding.lifecycleOwner = this

        shopActivity = activity as ShopActivity


        // 상품 리뷰 RecyclerView 구성 메서드 호출
        settingProductReviewRecyclerView()
        // 툴바 구성 메서드 호출
        settingToolbar()

        // 해당상품에대한 리뷰를 가져오는 메서드
        gettingReviewsOfProduct()

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

    fun gettingReviewsOfProduct() {
        fragmentProductReviewListBinding.shimmerFrameLayout.startShimmer()
        fragmentProductReviewListBinding.shimmerFrameLayout.visibility = View.VISIBLE
        fragmentProductReviewListBinding.recyclerViewProductReview.visibility = View.GONE

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val work1 = async(Dispatchers.IO) {
                    ProductService.gettingProductOne("8DynxD5PPXsWOYPXQFmF")
                }
                val productModel = work1.await()
                reviewsIdList = productModel.productReview

                reviewsIdList.forEach {
                    val work2 = async(Dispatchers.IO) {
                        ReviewService.gettingReviewByID(it)
                    }
                    reviewsModelList.add(work2.await())
                }

                reviewsModelList.forEach {
                    val imagesInOneReview = mutableListOf<Uri>()
                    it.reviewPhoto.forEach { fileName ->
                        val work3 = async(Dispatchers.IO) {
                            ReviewService.gettingReviewImage(fileName)
                        }
                        imagesInOneReview.add(work3.await())
                    }
                    reviewImagesMap[it.reviewDocumentID] = imagesInOneReview
                }

                fragmentProductReviewListBinding.recyclerViewProductReview.adapter?.notifyDataSetChanged()

            } catch (e: Exception) {
                Log.e("ProductReview", "Error fetching reviews", e)
                // 데이터 로드 실패 시 메시지 표시
                fragmentProductReviewListBinding.textViewProductReviewCnt.text = "리뷰를 불러올 수 없습니다."
            } finally {
                // Shimmer 중지 및 RecyclerView 상태 업데이트
                fragmentProductReviewListBinding.shimmerFrameLayout.stopShimmer()
                fragmentProductReviewListBinding.shimmerFrameLayout.visibility = View.GONE
                fragmentProductReviewListBinding.recyclerViewProductReview.visibility = View.VISIBLE
            }
        }
    }



    // 리뷰 쓰기 화면 이동 메서드
    fun moveToWriteReview() {
        // 상품 아이디 전달
    }

    // 사용자 리뷰 화면으로 이동하는 메서드
    fun moveToUserProductReviewFragment() {
        mainFragment.replaceFragment(ShopSubFragmentName.USER_PRODUCT_REVIEW_FRAGMENT, true, true, null)
    }

    // 상품 리뷰 목록 RecyclerView 구성 메서드
    fun settingProductReviewRecyclerView(){
        fragmentProductReviewListBinding.apply {
            recyclerViewProductReview.adapter = adapter
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
            return reviewsModelList.size
        }

        override fun onBindViewHolder(holder: ProductReviewViewHolder, position: Int) {
            val reviewModel = reviewsModelList[position]

            //리뷰별로 이미지리스트를 추출한다
            val imagesForReview = reviewImagesMap[reviewModel.reviewDocumentID] ?: mutableListOf()

            holder.rowProductReviewListBinding.rowProductReviewListViewModel?.rowTextViewProductReviewContentText?.value = reviewModel.reviewContent
            holder.rowProductReviewListBinding.rowProductReviewListViewModel?.rowTextViewProductReviewNameText?.value = reviewModel.reviewerName
            holder.rowProductReviewListBinding.rowProductReviewListViewModel?.rowRatingBarProductReviewRating?.value = reviewModel.reviewRating
            holder.rowProductReviewListBinding.rowProductReviewListViewModel?.rowTextViewProductReviewDateText?.value = reviewModel.reviewWriteDate

            // 중첩 RecyclerView에 이미지 리스트 설정
            holder.rowProductReviewListBinding.rowRecyclerViewProductReviewAttach.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = ProductReviewAttachRecyclerViewAdapter(imagesForReview) // 이미지 리스트 전달
            }

            // 클릭 이벤트 설정
            holder.rowProductReviewListBinding.rowTextViewProductReviewName.setOnClickListener {
                moveToUserProductReviewFragment()
            }
        }

    }

    // 상품 리뷰 사진 목록 RecyclerView 어답터
    inner class ProductReviewAttachRecyclerViewAdapter(
        private val imageUris: List<Uri> // 이미지 리스트를 전달받음
    ) : RecyclerView.Adapter<ProductReviewAttachRecyclerViewAdapter.ProductReviewAttachViewHolder>() {

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
            return imageUris.size // 이미지 리스트 크기를 반환
        }

        override fun onBindViewHolder(holder: ProductReviewAttachViewHolder, position: Int) {
            val imageUri = imageUris[position]

            // Glide를 사용하여 이미지 로드
            Glide.with(holder.rowProductReviewAttachBinding.root.context)
                .load(imageUri) // Firebase Storage URL
                .placeholder(R.drawable.img) // 로드 중 기본 이미지
                .error(R.drawable.img) // 로드 실패 시 기본 이미지
                .into(holder.rowProductReviewAttachBinding.rowImageViewProductReviewAttatch) // 대상 ImageView
        }
    }


}