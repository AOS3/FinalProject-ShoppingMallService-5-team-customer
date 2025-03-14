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
import com.judamie_user.android.databinding.FragmentUserProductReviewListBinding
import com.judamie_user.android.databinding.RowProductReviewAttachBinding
import com.judamie_user.android.databinding.RowProductReviewListBinding
import com.judamie_user.android.databinding.RowUserProductReviewListBinding
import com.judamie_user.android.firebase.model.ReviewModel
import com.judamie_user.android.firebase.service.ProductService
import com.judamie_user.android.firebase.service.ReviewService
import com.judamie_user.android.firebase.service.UserService
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.viewmodel.fragmentviewmodel.UserProductReviewListViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowProductReviewListViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowUserProductReviewListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


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

    //해당 유저의 리뷰 모델을 가져온다
    var reviewModelList = mutableListOf<ReviewModel>()

    //리뷰에있는 제품아이디로 제품이름을 가져와서 매핑한다
    var reviewProductNameMap = mutableMapOf<String, String>()

    // 이미지 파일을 리뷰별로 분류한다
    var reviewImagesMap = mutableMapOf<String, MutableList<Uri>>()

    //유저 id
    var userDocumentID=""

    //툴바 타이틀
    var toolbarTitle = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentUserProductReviewListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_user_product_review_list,
            container,
            false
        )
        fragmentUserProductReviewListBinding.userProductReviewViewModel =
            UserProductReviewListViewModel(this)
        fragmentUserProductReviewListBinding.lifecycleOwner = viewLifecycleOwner

        shopActivity = activity as ShopActivity

        // 상품 리뷰 RecyclerView 구성 메서드 호출
        settingProductReviewRecyclerView()

        if (toolbarTitle == ""){
            // 툴바 구성 메서드 호출
            settingToolbar()
            Log.d("test","툴바구성 호출됨")
        }else{
            fragmentUserProductReviewListBinding.userProductReviewViewModel?.toolbarUserProductReviewTitle?.value = toolbarTitle
        }

        if (reviewModelList.isEmpty() && reviewProductNameMap.isEmpty() && reviewImagesMap.isEmpty()) {
            //리스트가 바워져있을때만 데이터를 불러와 리사이클러뷰를 구성한다
            gettingReviewsOfUser()
        } else {
            //다시돌아왔을 경우에는 이미세팅된리사이클러뷰를 보여준다
            fragmentUserProductReviewListBinding.apply {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                recyclerViewUserProductReview.visibility = View.VISIBLE
            }
        }

        // 유저 id를 가져온다
        gettingProductID()


        return fragmentUserProductReviewListBinding.root
    }
    // 유저 id를 가져온다
    fun gettingProductID(){
        userDocumentID = arguments?.getString("userDocumentId")!!
    }

    // 툴바 구성 메서드
    fun settingToolbar() {
        fragmentUserProductReviewListBinding.userProductReviewViewModel?.apply {
            CoroutineScope(Dispatchers.Main).launch {
                val work = async(Dispatchers.IO) {
                    UserService.gettingUserNameByID(userDocumentID)
                }
                val userName = work.await()
                //Log.d("test", userName)
                toolbarTitle = "${userName}님의 리뷰"
                toolbarUserProductReviewTitle?.value = toolbarTitle

            }
        }

    }


    //유저별 리뷰를 가져오는 메서드
    private fun gettingReviewsOfUser() {

//       //해당 유저의 리뷰 모델을 가져온다
//    var reviewModelList = mutableListOf<ReviewModel>()
//
//    //리뷰에있는 제품아이디로 제품이름을 가져와서 매핑한다
//    var reviewProductNameMap = mutableMapOf<String, String>()
//
//    // 이미지 파일을 리뷰별로 분류한다
//    var reviewImagesMap = mutableMapOf<String,MutableList<Uri>>()
//        reviewModelList.clear()
//        reviewProductNameMap.clear()
//        reviewImagesMap.clear()

        fragmentUserProductReviewListBinding.apply {
            shimmerFrameLayout.startShimmer()
            shimmerFrameLayout.visibility = View.VISIBLE
            recyclerViewUserProductReview.visibility = View.GONE




            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val work1 = async(Dispatchers.IO) {
                        ReviewService.gettingReviewListByOneUser(userDocumentID)
                    }
                    reviewModelList = work1.await()



                    //리뷰에 있는 제품id를 통해 제품 이름을 가져온다
                    reviewModelList.forEach {
                        val work2 = async(Dispatchers.IO) {
                            ProductService.gettingProductName(it.reviewProductDocumentID)
                        }
                        val productName = work2.await()
                        if (productName != null) {
                            reviewProductNameMap.put(it.reviewProductDocumentID, productName)
                        }
                    }

                    //리뷰에있는 사진을 가져온다
                    reviewModelList.forEach { it ->
                        val imagesInOneReview = mutableListOf<Uri>()
                        it.reviewPhoto.forEach { fileName ->
                            val work3 = async(Dispatchers.IO) {
                                ReviewService.gettingReviewImage(fileName)
                            }
                            imagesInOneReview.add(work3.await())
                        }
                        reviewImagesMap[it.reviewProductDocumentID] = imagesInOneReview
                    }
                    reviewImagesMap.forEach{
                        Log.d("test",it.value.toString())
                    }


                    fragmentUserProductReviewListBinding.recyclerViewUserProductReview.adapter?.notifyDataSetChanged()

                } catch (e: Exception) {
                    Log.e("ProductReview", "Error fetching reviews", e)
                } finally {
                    // Shimmer 중지 및 RecyclerView 상태 업데이트
                    fragmentUserProductReviewListBinding.shimmerFrameLayout.stopShimmer()
                    fragmentUserProductReviewListBinding.shimmerFrameLayout.visibility =
                        View.GONE
                    fragmentUserProductReviewListBinding.recyclerViewUserProductReview.visibility =
                        View.VISIBLE
                }
            }
        }
    }

    // 상품 리뷰 목록 RecyclerView 구성 메서드
    fun settingProductReviewRecyclerView() {
        fragmentUserProductReviewListBinding.apply {
            recyclerViewUserProductReview.adapter = UserReviewRecyclerViewAdapter()
            recyclerViewUserProductReview.layoutManager = LinearLayoutManager(shopActivity)
            val deco =
                MaterialDividerItemDecoration(
                    shopActivity,
                    MaterialDividerItemDecoration.VERTICAL
                )
            recyclerViewUserProductReview.addItemDecoration(deco)

        }
    }

    // 상품 리뷰 목록 RecyclerView 어답터
    inner class UserReviewRecyclerViewAdapter :
        RecyclerView.Adapter<UserReviewRecyclerViewAdapter.UserReviewViewHolder>() {
        inner class UserReviewViewHolder(val rowUserProductReviewListBinding: RowUserProductReviewListBinding) :
            RecyclerView.ViewHolder(rowUserProductReviewListBinding.root)

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): UserReviewViewHolder {
            val rowUserReviewListBinding =
                DataBindingUtil.inflate<RowUserProductReviewListBinding>(
                    layoutInflater,
                    R.layout.row_user_product_review_list,
                    parent,
                    false
                )
            rowUserReviewListBinding.rowUserProductReviewListViewModel =
                RowUserProductReviewListViewModel(this@UserProductReviewListFragment)
            rowUserReviewListBinding.lifecycleOwner = this@UserProductReviewListFragment

            val userReviewViewHolder = UserReviewViewHolder(rowUserReviewListBinding)

            return userReviewViewHolder
        }

        override fun getItemCount(): Int {
            return reviewModelList.size
        }

        override fun onBindViewHolder(holder: UserReviewViewHolder, position: Int) {

            val reviewModel = reviewModelList[position]

            //리뷰별로 이미지리스트를 추출한다
            val imagesForReview =
                reviewImagesMap[reviewModel.reviewProductDocumentID] ?: mutableListOf()

            for (j in reviewProductNameMap) {
                if (reviewModelList[position].reviewProductDocumentID == j.key) {
                    holder.rowUserProductReviewListBinding.rowUserProductReviewListViewModel?.rowTextViewUserProductReviewProductNameText?.value =
                        j.value
                    break
                }
            }
            holder.rowUserProductReviewListBinding.rowUserProductReviewListViewModel?.rowTextViewUserProductReviewContentText?.value =
                reviewModelList[position].reviewContent
            holder.rowUserProductReviewListBinding.rowUserProductReviewListViewModel?.rowRatingBarUserProductReviewRating?.value =
                reviewModelList[position].reviewRating
            holder.rowUserProductReviewListBinding.rowUserProductReviewListViewModel?.rowTextViewUserProductReviewDateText?.value =
                reviewModelList[position].reviewWriteDate
            // Nested RecyclerView 설정
            holder.rowUserProductReviewListBinding.rowRecyclerViewUserProductReviewAttach.apply {
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = ProductReviewAttachRecyclerViewAdapter(imagesForReview) // 사진 데이터 전달
            }
        }
    }

    // 상품 리뷰 사진 목록 RecyclerView 어답터
    inner class ProductReviewAttachRecyclerViewAdapter(private val imageUris: List<Uri>) :
        RecyclerView.Adapter<ProductReviewAttachRecyclerViewAdapter.ProductReviewAttachViewHolder>() {

        inner class ProductReviewAttachViewHolder(val rowProductReviewAttachBinding: RowProductReviewAttachBinding) :
            RecyclerView.ViewHolder(rowProductReviewAttachBinding.root){
                init {
                    rowProductReviewAttachBinding.root.apply {
                        setOnClickListener {
                            val imageUri =  imageUris[adapterPosition]
                            val bundle = Bundle()
                            bundle.putParcelable("image_uri", imageUri)
                            mainFragment.replaceFragment(ShopSubFragmentName.REVIEW_IMAGE_FRAGMENT,true,true,bundle)
                        }
                    }
                }
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductReviewAttachViewHolder {
            val binding = DataBindingUtil.inflate<RowProductReviewAttachBinding>(LayoutInflater.from(parent.context), R.layout.row_product_review_attach, parent, false)
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