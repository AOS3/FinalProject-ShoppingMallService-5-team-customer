package com.judamie_user.android.ui.fragment

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.judamie_user.android.R
import com.judamie_user.android.activity.ShopActivity
import com.judamie_user.android.databinding.FragmentWishListBinding
import com.judamie_user.android.databinding.RowProductListBinding
import com.judamie_user.android.firebase.model.ProductModel
import com.judamie_user.android.firebase.repository.UserRepository
import com.judamie_user.android.firebase.service.ProductService
import com.judamie_user.android.firebase.service.UserService
import com.judamie_user.android.util.tools.Companion.formatToComma
import com.judamie_user.android.viewmodel.fragmentviewmodel.WishListViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowProductListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait

class WishListFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentWishListBinding: FragmentWishListBinding
    lateinit var shopActivity: ShopActivity

    //  찜목록RecyclerView를 구성하기 위해 사용할 리스트
    var recyclerViewWishList = Array(50, {
        "항목 ${it + 1}"
    })

    //유저의 wishlist(제품id)
    var userWishListProductID = mutableListOf<String>()

    //유저의 wishlist(제품Model)
    var userWishListProductModel = mutableListOf<ProductModel>()

    //제품 모델과 이미지 uri 맵
    var productModelImageMap = mutableMapOf<ProductModel, Uri>()

    //제품 모델과 이미지 uri 맵을 리스트로 바꾸기위한 리스트
    var productModelImageList = mutableListOf<Pair<ProductModel, Uri>>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentWishListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_wish_list, container, false)
        fragmentWishListBinding.wishListViewModel = WishListViewModel(this)
        fragmentWishListBinding.lifecycleOwner = this
        shopActivity = activity as ShopActivity


        //스크롤 위에서 아래로잡아당겨서 업데이트
        refreshData()

        //툴바설정
        settingMaterialToolbarWishList()

        //리사이클러뷰 세팅
        settingRecyclerViewWishList()


        if (userWishListProductID.isEmpty()) {
            //유저의 찜목록을 가져온다
            CoroutineScope(Dispatchers.Main).launch {
                gettingUserWishList()
            }
        }else{
            fragmentWishListBinding.progressBarWishList.visibility = View.GONE
        }




        return fragmentWishListBinding.root
    }

    //툴바설정
    private fun settingMaterialToolbarWishList() {
        fragmentWishListBinding.apply {
            //메뉴의 항목을 눌렀을때
            materialToolbarWishList.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuItemWishListNotification -> {
                        mainFragment.replaceFragment(
                            ShopSubFragmentName.USER_NOTIFICATION_LIST_FRAGMENT,
                            true,
                            true,
                            null
                        )
                    }

                    R.id.menuItemWishListShoppingCart -> {
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


    private fun refreshData() {
        // 찜 목록이 비어있다면 스와이프 비활성화
        if (recyclerViewWishList.isEmpty()) {
            fragmentWishListBinding.swipeRefreshLayout.isEnabled = false
        } else {
            fragmentWishListBinding.apply {
                swipeRefreshLayout.setOnRefreshListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        swipeRefreshLayout.isRefreshing = true // 로딩 애니메이션 시작
                        recyclerViewWishList.visibility = View.INVISIBLE
                        progressBarWishList.visibility = View.VISIBLE
                        swipeRefreshLayout.isRefreshing = false // 작업 완료 후 애니메이션 종료
                        try {
                            // 찜 목록 가져오기 (suspend 함수 호출)
                            userWishListProductID.clear()
                            userWishListProductModel.clear()
                            productModelImageMap.clear()
                            productModelImageList.clear()
                            gettingUserWishList()
                        } catch (e: Exception) {
                            e.printStackTrace() // 예외 처리
                        } finally {
                            recyclerViewWishList.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    // 유저의 찜목록을 가져오는 suspend 함수
    private suspend fun gettingUserWishList() {
        fragmentWishListBinding.apply {
            // 1. 유저 컬렉션에서 유저의 제품 ID가 담긴 리스트를 가져온다
            userWishListProductID = UserService.gettingWishListByUserID(shopActivity.userDocumentID)

            // 2. 제품 ID 리스트를 기반으로 제품 모델 리스트를 가져온다
            userWishListProductID.forEach {
                val product = withContext(Dispatchers.IO) {
                    ProductService.gettingProductOne(it)
                }
                userWishListProductModel.add(product)
            }

            // 3. 제품 모델 리스트를 기반으로 이미지 리스트를 가져와 매핑한다
            userWishListProductModel.forEach {
                val image = withContext(Dispatchers.IO) {
                    ProductService.gettingImage(it.productMainImage)
                }
                productModelImageMap[it] = image
            }

            productModelImageList = productModelImageMap.toList().toMutableList()

            // 찜 목록이 비어 있을 경우 UI 업데이트
            withContext(Dispatchers.Main) {
                if (productModelImageList.isEmpty()) {
                    fragmentWishListBinding.apply {
                        textViewEmptyWishList1.visibility = View.VISIBLE
                    }
                }else{
                    fragmentWishListBinding.apply {
                        textViewEmptyWishList1.visibility = View.GONE
                    }
                }

                // RecyclerView 업데이트
                fragmentWishListBinding.recyclerViewWishList.adapter?.notifyDataSetChanged()
                progressBarWishList.visibility = View.GONE
            }
        }

    }


    //찜 목록 리사이클러뷰를 세팅한다
    fun settingRecyclerViewWishList() {
        fragmentWishListBinding.apply {
            recyclerViewWishList.layoutManager = GridLayoutManager(requireContext(), 2) // 2열
            recyclerViewWishList.adapter = RecyclerViewAdapter()
        }
    }

    inner class RecyclerViewAdapter :
        RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {

        inner class RecyclerViewHolder(val rowProductListBinding: RowProductListBinding) :
            RecyclerView.ViewHolder(rowProductListBinding.root) {
            init {
                // 초기 상태 설정
                rowProductListBinding.imageButtonSearchSetWishList.apply {
                    setImageResource(R.drawable.bookmark_filled_24px) // 초기 아이콘
                    imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(itemView.context, R.color.mainColor)
                    )
                    tag = "filled" // 초기 태그
                }

                // 클릭 이벤트 설정
                rowProductListBinding.imageButtonSearchSetWishList.setOnClickListener {
                    rowProductListBinding.apply {
                        val isFilled = imageButtonSearchSetWishList.tag == "filled"
                        val productId = productModelImageList[adapterPosition].first.productDocumentId

                        // UI를 즉시 업데이트
                        if (isFilled) {
                            // Outline으로 변경
                            imageButtonSearchSetWishList.setImageResource(R.drawable.bookmark_outline_24px)
                            imageButtonSearchSetWishList.imageTintList = ColorStateList.valueOf(
                                ContextCompat.getColor(itemView.context, R.color.mainColor)
                            )
                            imageButtonSearchSetWishList.tag = "outline" // 태그 업데이트
                            Toast.makeText(requireContext(), "즐겨찾기에서 삭제되었습니다", Toast.LENGTH_SHORT).show()

                            // DB에서 삭제 작업 (백그라운드 처리)
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    UserService.deleteUserWishList(shopActivity.userDocumentID, productId)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    // 오류가 발생하면 UI 상태를 복구
                                    withContext(Dispatchers.Main) {
                                        imageButtonSearchSetWishList.setImageResource(R.drawable.bookmark_filled_24px)
                                        imageButtonSearchSetWishList.tag = "filled"
                                    }
                                }
                            }
                        } else {
                            // Filled로 변경
                            imageButtonSearchSetWishList.setImageResource(R.drawable.bookmark_filled_24px)
                            imageButtonSearchSetWishList.imageTintList = ColorStateList.valueOf(
                                ContextCompat.getColor(itemView.context, R.color.mainColor)
                            )
                            imageButtonSearchSetWishList.tag = "filled" // 태그 업데이트
                            Toast.makeText(requireContext(), "즐겨찾기에 추가되었습니다", Toast.LENGTH_SHORT).show()
                            // DB에 추가 작업 (백그라운드 처리)
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    UserService.addUserWishList(shopActivity.userDocumentID, productId)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    // 오류가 발생하면 UI 상태를 복구
                                    withContext(Dispatchers.Main) {
                                        imageButtonSearchSetWishList.setImageResource(R.drawable.bookmark_outline_24px)
                                        imageButtonSearchSetWishList.tag = "outline"
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
            val rowProductListBinding = DataBindingUtil.inflate<RowProductListBinding>(
                layoutInflater,
                R.layout.row_product_list,
                parent,
                false
            )
            rowProductListBinding.rowProductListViewModel =
                RowProductListViewModel(this@WishListFragment)
            rowProductListBinding.lifecycleOwner = viewLifecycleOwner

            val recyclerViewHolder = RecyclerViewHolder(rowProductListBinding)

            // 리사이클러뷰 항목 클릭시 상세 거래 완료 내역 보기 화면으로 이동
            rowProductListBinding.root.setOnClickListener {
                // 사용자가 누른 항목의 게시글 문서 번호를 담아서 전달
                val dataBundle = Bundle()
                dataBundle.putString(
                    "productDocumentId",
                    productModelImageList[recyclerViewHolder.adapterPosition].first.productDocumentId
                )


                mainFragment.replaceFragment(
                    ShopSubFragmentName.PRODUCT_INFO_FRAGMENT,
                    true,
                    true,
                    dataBundle
                )
            }

            return recyclerViewHolder
        }

        override fun getItemCount(): Int {
            return productModelImageList.size
        }

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            holder.rowProductListBinding.apply {
                rowProductListViewModel?.textViewSearchProductNameText?.value =
                    productModelImageList[position].first.productName

                val discount = productModelImageList[position].first.productDiscountRate
                rowProductListViewModel?.textViewSearchDiscountRatingText?.value =
                    if (discount > 0) "${discount}%" else ""
                textViewSearchDiscountRating.visibility =
                    if (discount > 0) View.VISIBLE else View.GONE


                if(discount>0){
                    val realPrice = productModelImageList[position].first.productPrice - ((discount.toFloat()/100)*productModelImageList[position].first.productPrice)
                    Log.d("realPrice","${realPrice}, ${productModelImageList[position].first.productPrice}")
                    holder.rowProductListBinding.rowProductListViewModel?.textViewSearchProductPriceText?.value =
                        realPrice.formatToComma()
                }else{
                    holder.rowProductListBinding.rowProductListViewModel?.textViewSearchProductPriceText?.value =
                        productModelImageList[position].first.productPrice.formatToComma()
                }

                val reviewSize = productModelImageList[position].first.productReview.size
                rowProductListViewModel?.textViewSearchProductReviewText?.value = if (reviewSize > 0) "리뷰 ($reviewSize)" else ""
                textViewSearchProductReview.visibility = if (reviewSize > 0) View.VISIBLE else View.GONE

                rowProductListViewModel?.textViewSearchProductSellerText?.value =
                    productModelImageList[position].first.productSeller
            }


            //val imageUri = imageUris[position]
            //
            //            // Glide를 사용하여 이미지 로드
            //            Glide.with(holder.rowProductReviewAttachBinding.root.context)
            //                .load(imageUri) // Firebase Storage URL
            //                .placeholder(R.drawable.img) // 로드 중 기본 이미지
            //                .error(R.drawable.img) // 로드 실패 시 기본 이미지
            //                .into(holder.rowProductReviewAttachBinding.rowImageViewProductReviewAttatch)

            val imageUri = productModelImageList[position].second

            Glide.with(holder.rowProductListBinding.root.context)
                .load(imageUri)
                .placeholder(R.drawable.img)
                .error(R.drawable.img)
                .into(holder.rowProductListBinding.imageViewSearchProduct)
        }
    }


}