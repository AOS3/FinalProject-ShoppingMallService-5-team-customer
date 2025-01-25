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
import androidx.core.content.ContextCompat
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
import com.judamie_user.android.viewmodel.fragmentviewmodel.WishListViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowProductListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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
    var productModelImageMap = mutableMapOf<ProductModel,Uri>()

    //제품 모델과 이미지 uri 맵을 리스트로 바꾸기위한 리스트
    var list = listOf<Pair<ProductModel, Uri>>()


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

        if(userWishListProductID.isEmpty()){
            //유저의 찜목록을 가져온다
            gettingUserWishList()
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
                        mainFragment.replaceFragment(ShopSubFragmentName.USER_NOTIFICATION_LIST_FRAGMENT,
                            true,
                            true,
                            null)
                    }
                    R.id.menuItemWishListShoppingCart ->{
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

    //스크롤 위에서 아래로잡아당겨서 업데이트
    private fun refreshData() {
        //찜 목록이 비어있다면 스와이프 비활성화
        if (recyclerViewWishList.size == 0) {
            fragmentWishListBinding.swipeRefreshLayout.isEnabled = false
        }
        if (recyclerViewWishList.size > 0) {
            fragmentWishListBinding.apply {
                swipeRefreshLayout.setOnRefreshListener {

                    Handler(Looper.getMainLooper()).postDelayed({
                        // 새로 고침 완료 시 로딩 애니메이션 종료
                        fragmentWishListBinding.swipeRefreshLayout.isRefreshing = false
                    }, 500) // 0.5초 지연 후 종료
                }
            }
        }
    }

    //유저의 찜목록을 가져온다
    private fun gettingUserWishList(){

        CoroutineScope(Dispatchers.Main).launch {
            // 1.유저 컬렉션에서 유저의 제품ID가 담긴 리스트를 가져온다
            val work1 = async (Dispatchers.IO){
                UserService.gettingWishListByUserID(shopActivity.userDocumentID)
            }
            userWishListProductID = work1.await()

            // 2. 1에서 가져온 리스트를 토대로 제품modelList를 가져온다
            userWishListProductID.forEach {
                val work2 = async (Dispatchers.IO){
                    ProductService.gettingProductOne(it)
                }
                userWishListProductModel.add(work2.await())
            }

            // 3. 2에서 가져온 리스트를 토대로 제품이미지를 가져와서 모델과 매핑한다
            userWishListProductModel.forEach {
                val work3 = async (Dispatchers.IO){
                    ProductService.gettingImage(it.productMainImage)
                }
                productModelImageMap[it] = work3.await()
            }

            list = productModelImageMap.toList()

            list.forEach{
                Log.d("test",it.first.productSeller)
                Log.d("test",it.second.toString())
                Log.d("test",it.first.productName)
            }

            if (list.isEmpty()){
                fragmentWishListBinding.apply {
                    textViewEmptyWishList1.visibility = View.VISIBLE
                }
            }


            fragmentWishListBinding.recyclerViewWishList.adapter?.notifyDataSetChanged()




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
                rowProductListBinding.imageButtonSearchSetWishList.apply {
                    setImageResource(R.drawable.bookmark_filled_24px) // 초기 아이콘
                    imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(itemView.context, R.color.mainColor)
                    )
                    tag = "filled" // 초기 태그
                }
                rowProductListBinding.apply {
                    imageButtonSearchSetWishList.setOnClickListener {

                        val isFilled = imageButtonSearchSetWishList.tag == "filled"

                        if (isFilled) {
                            // Outline으로 변경
                            imageButtonSearchSetWishList.setImageResource(R.drawable.bookmark_outline_24px)
                            imageButtonSearchSetWishList.imageTintList = ColorStateList.valueOf(
                                ContextCompat.getColor(itemView.context, R.color.mainColor)
                            )
                            imageButtonSearchSetWishList.tag = "outline" // 태그 업데이트
                        } else {
                            // Filled로 변경
                            imageButtonSearchSetWishList.setImageResource(R.drawable.bookmark_filled_24px)
                            imageButtonSearchSetWishList.imageTintList = ColorStateList.valueOf(
                                ContextCompat.getColor(itemView.context, R.color.mainColor)
                            )
                            imageButtonSearchSetWishList.tag = "filled" // 태그 업데이트
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
            rowProductListBinding.viewModel = RowProductListViewModel(this@WishListFragment)
            rowProductListBinding.lifecycleOwner = viewLifecycleOwner

            // 리사이클러뷰 항목 클릭시 상세 거래 완료 내역 보기 화면으로 이동
            rowProductListBinding.root.setOnClickListener {
                // 사용자가 누른 항목의 게시글 문서 번호를 담아서 전달
                val dataBundle = Bundle()
//                dataBundle.putString("boardDocumentId", recyclerViewList[mainViewHolder.adapterPosition].boardDocumentId)
//
                mainFragment.replaceFragment(
                    ShopSubFragmentName.PRODUCT_INFO_FRAGMENT,
                    true,
                    true,
                    dataBundle
                )
            }

            return RecyclerViewHolder(rowProductListBinding)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            holder.rowProductListBinding.viewModel?.textViewSearchProductNameText?.value =
                list[position].first.productName

            if (list[position].first.productDiscountRate != 0){
                holder.rowProductListBinding.viewModel?.textViewSearchDiscountRatingText?.value = "${list[position].first.productDiscountRate}%"
            }else{
                holder.rowProductListBinding.viewModel?.textViewSearchDiscountRatingVisibility = View.GONE
            }

            val finalPrice = holder.rowProductListBinding.viewModel?.calculateProductPrice(list[position].first.productDiscountRate,list[position].first.productPrice)
            holder.rowProductListBinding.viewModel?.textViewSearchProductPriceText?.value = "${finalPrice}원"

            holder.rowProductListBinding.viewModel?.textViewSearchProductReviewCountText?.value =
                list[position].first.productReview.size.toString()
            holder.rowProductListBinding.viewModel?.textViewSearchProductSellerText?.value =
                list[position].first.productSeller

            //val imageUri = imageUris[position]
            //
            //            // Glide를 사용하여 이미지 로드
            //            Glide.with(holder.rowProductReviewAttachBinding.root.context)
            //                .load(imageUri) // Firebase Storage URL
            //                .placeholder(R.drawable.img) // 로드 중 기본 이미지
            //                .error(R.drawable.img) // 로드 실패 시 기본 이미지
            //                .into(holder.rowProductReviewAttachBinding.rowImageViewProductReviewAttatch)

            val imageUri = list[position].second

            Glide.with(holder.rowProductListBinding.root.context)
                .load(imageUri)
                .placeholder(R.drawable.img) // 로드 중 기본 이미지
                .error(R.drawable.img)
                .into(holder.rowProductListBinding.imageViewSearchProduct)
        }
    }



}