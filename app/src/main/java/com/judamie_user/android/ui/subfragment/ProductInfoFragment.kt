package com.judamie_user.android.ui.subfragment

import android.content.Context
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.judamie_user.android.R
import com.judamie_user.android.activity.ShopActivity
import com.judamie_user.android.databinding.FragmentProductInfoBinding
import com.judamie_user.android.databinding.RowProductIntoImgListBinding
import com.judamie_user.android.firebase.model.ProductModel
import com.judamie_user.android.firebase.model.UserModel
import com.judamie_user.android.firebase.service.CouponService
import com.judamie_user.android.firebase.service.ProductService
import com.judamie_user.android.firebase.service.UserService
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopCartFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.viewmodel.componentviewmodel.CartIdCountViewModel
import com.judamie_user.android.viewmodel.componentviewmodel.CartViewModel
import com.judamie_user.android.viewmodel.fragmentviewmodel.ProductInfoViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowProductInfoImgListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ProductInfoFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentProductInfoBinding: FragmentProductInfoBinding
    lateinit var shopActivity: ShopActivity

    // 상품 초기 수량
    var productCount: Int = 1

//    // ReyclerView 구성을 위한 임시 데이터
//    val tempImgList1 = Array(5) {
//        R.drawable.sampleproductimage_gp18
//    }

    // RecyclerView를 구성하기 위해 사용할 Uri 리스트
    var recyclerViewList = MutableLiveData<MutableList<Uri>>()

    // 서버에서 받아온 데이터를 담을 변수
    lateinit var productModel: ProductModel


    // 번들로 전달된 데이터를 담을 변수 : 상품 문서 id
    lateinit var productDocumentId: String

    // 사용자 정보를 담을 변수
    lateinit var userModel: UserModel


    private lateinit var cartIdCountViewModel: CartIdCountViewModel

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


        // ViewModel 가져오기
        cartIdCountViewModel =
            ViewModelProvider(requireActivity()).get(CartIdCountViewModel::class.java)

        // 툴바 구성 메서드 호출
        settingToolbar()
        // arguments의 값을 변수에 담아주는 메서드 호출
        gettingArguments()
        // 데이터를 가져와 RecyclerView를 갱신하는 메서드
        refreshRecyclerView()
        // 상품 이미지 RecyclerView 구성 메서드 호출
        settingCartRecyclerView()
        // 화면 구성 메서드 호출
        settingData()
        // RatingBar 속성 설정
        setupRatingBar()
        // Firebase에서 찜 상태 확인 및 초기 아이콘 설정
        checkWishListStatus()

        return fragmentProductInfoBinding.root
    }

    // arguments의 값을 변수에 담아준다.
    fun gettingArguments() {
        productDocumentId = arguments?.getString("productDocumentId")!!
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
            dataBundle.putString("productDocumentId",productDocumentId)
            mainFragment.replaceFragment(
                ShopSubFragmentName.PRODUCT_REVIEW_LIST_FRAGMENT,
                true,
                true,
                dataBundle
            )
        }

    }
    // Firebase에서 찜 상태 가져오고 ViewModel에 설정
    private fun checkWishListStatus() {
        CoroutineScope(Dispatchers.Main).launch {
            val work = async(Dispatchers.IO) {
                UserService.gettingWishListByUserID(shopActivity.userDocumentID)
            }
            val userWishProductIDList = work.await()
            val isWishListed = userWishProductIDList.contains(productDocumentId)

            // ViewModel에 초기 상태 설정
            fragmentProductInfoBinding.productInfoViewModel?.setWishListStatus(isWishListed)
        }
    }

    // Firebase 업데이트 개선 (UI가 즉시 반영되도록 수정)
    fun updateWishListInFirebase(isWishListed: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            if (isWishListed) {
                UserService.addUserWishList(shopActivity.userDocumentID, productDocumentId)
            } else {
                UserService.deleteUserWishList(shopActivity.userDocumentID, productDocumentId)
            }
        }
    }

    // 화면 구성 메서드
    fun settingData() {
        // 서버에서 데이터를 가져온다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                ProductService.selectProductDataOneById(productDocumentId)
            }
            productModel = work1.await()

            // 할인가격 계산하기
            val productSalePrice =
                ((100 - productModel.productDiscountRate) * productModel.productPrice * 0.01).toInt()

            fragmentProductInfoBinding.apply {
                productInfoViewModel?.textViewProductInfoProductCategoryText?.value =
                    productModel.productCategory
                productInfoViewModel?.textViewProductInfoProductNameText?.value =
                    productModel.productName
                productInfoViewModel?.textViewProductInfoDiscountPercentText?.value =
                    productModel.productDiscountRate.toString()
                productInfoViewModel?.textViewProductInfoPriceText?.value =
                    productSalePrice.toString()
                productInfoViewModel?.textViewProductInfoDescriptionText?.value =
                    productModel.productDescription
            }

            // 메인이미지 불러오기
            val work2 = async(Dispatchers.IO) {
                // 이미지에 접근할 수 있는 uri를 가져온다.
                ProductService.gettingImage(productModel.productMainImage)
            }

            val imageUri = work2.await()
            shopActivity.showServiceImage(
                imageUri, fragmentProductInfoBinding.imageViewProductInfoThumbnail
            )
        }
    }

    // 툴바 구성 메서드
    fun settingToolbar() {
        fragmentProductInfoBinding.productInfoViewModel?.apply {
            // 타이틀
            toolbarProductInfoTitle.value = "제품 상세"
            // 네비게이션 아이콘
            toolbarProductInfoNavigationIcon.value = R.drawable.arrow_back_ios_24px
        }
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment() {
        mainFragment.removeFragment(ShopSubFragmentName.PRODUCT_INFO_FRAGMENT)
    }

    // 상품 수량 선택 메서드
    fun selectProductCount(isIncrease: Boolean) {
        fragmentProductInfoBinding.apply {
            val currentCount =
                productInfoViewModel?.textViewProductInfoCntText?.value?.toIntOrNull() ?: 1

            if (isIncrease) {
                // 수량 증가
                productInfoViewModel?.textViewProductInfoCntText?.value =
                    (currentCount + 1).toString()
            } else {
                // 수량 감소 (1 이하로는 감소하지 않음)
                if (currentCount > 1) {
                    productInfoViewModel?.textViewProductInfoCntText?.value =
                        (currentCount - 1).toString()
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
        bundle.putString(
            "productCount",
            fragmentProductInfoBinding.productInfoViewModel?.textViewProductInfoCntText?.value
        )
        bundle.putString("productDocumentId", productDocumentId)

        // 결제 화면으로 이동
        mainFragment.replaceFragment(
            ShopSubFragmentName.PAYMENT_PRODUCT_FRAGMENT,
            true,
            true,
            bundle
        )
    }

    // 장바구니 넣기 메서드
    fun addCartProduct() {
        fragmentProductInfoBinding.apply {
            // 동일한 상품이 장바구니에 없을 경우
            // 일단 로그인한 사용자 정보 가져와서 장바구니,, 목록 살펴보기
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO) {
                    UserService.selectUserDataByUserDocumentIdOne(shopActivity.userDocumentID)
                }
                userModel = work1.await()

                if (productDocumentId in userModel.userCartList) {
                    // 동일한 상품이 장바구니에 있을 경우
                    // WishList 상품 추가X + 토스트 표시 : "장바구니에 동일한 상품이 있습니다."
                    Toast.makeText(shopActivity, "장바구니에 동일한 상품이 있습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    // WishList 상품 추가O + 토스트 표시 : "장바구니에 상품이 추가되었습니다"
                    // 서버에 추가하기
                    userModel.userCartList.add(productDocumentId)
                    UserService.addCartData(userModel)
                    Toast.makeText(shopActivity, "장바구니에 상품이 추가되었습니다", Toast.LENGTH_SHORT).show()


//                    // 장바구니에 담을 상품과 수량 맵 생성
//                    val cartMap = HashMap<String, Int>()
//                    cartMap[productDocumentId] = fragmentProductInfoBinding. productInfoViewModel?.textViewProductInfoCntText?.value!!.toInt()
//
//                    // 기존 ShopCartFragment 인스턴스를 찾아 데이터 전달
//                    val shopCartFragment = parentFragmentManager.findFragmentByTag("ShopCartFragment") as? ShopCartFragment
//                    shopCartFragment?.updateCart(cartMap)

                    val quantity =
                        fragmentProductInfoBinding.productInfoViewModel?.textViewProductInfoCntText?.value!!.toInt()
                    cartIdCountViewModel.addToCart(productDocumentId, quantity)


                    // cartMap 값 확인
                    // Log.d("cartMap", ": $cartMap")
                }
            }
        }
    }

    // 데이터를 가져와 RecyclerView를 갱신하는 메서드
    fun refreshRecyclerView() {
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                // productDocumentId와 일치하는 데이터 상세 사진 가져오기
                ProductService.gettingSubImage(productDocumentId)
            }
            recyclerViewList.postValue(work1.await().toMutableList())

            fragmentProductInfoBinding.recyclerViewProductInfoImg.adapter?.notifyDataSetChanged()
        }
    }

    // 상품 상세 이미지 RecyclerView 구성 메서드
    fun settingCartRecyclerView() {
        fragmentProductInfoBinding.apply {
            recyclerViewProductInfoImg.adapter = ProductImgRecyclerViewAdapter()
            recyclerViewProductInfoImg.layoutManager = LinearLayoutManager(shopActivity)

            // 구분선 추가 X
            // val deco = MaterialDividerItemDecoration(shopActivity, MaterialDividerItemDecoration.VERTICAL)
            // recyclerViewProductInfoImg.addItemDecoration(deco)
        }
    }

    // 상품 상세 이미지 RecyclerView 어답터
    inner class ProductImgRecyclerViewAdapter :
        RecyclerView.Adapter<ProductImgRecyclerViewAdapter.ProductImgViewHolder>() {
        inner class ProductImgViewHolder(val rowProductInfoImgListBinding: RowProductIntoImgListBinding) :
            RecyclerView.ViewHolder(rowProductInfoImgListBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImgViewHolder {
            val rowProductInfoImgListBinding =
                DataBindingUtil.inflate<RowProductIntoImgListBinding>(
                    layoutInflater,
                    R.layout.row_product_into_img_list,
                    parent,
                    false
                )
            rowProductInfoImgListBinding.rowProductInfoImgListViewModel =
                RowProductInfoImgListViewModel(this@ProductInfoFragment)
            rowProductInfoImgListBinding.lifecycleOwner = this@ProductInfoFragment

            val productImgViewHolder = ProductImgViewHolder(rowProductInfoImgListBinding)

            return productImgViewHolder
        }

        override fun getItemCount(): Int {
            return recyclerViewList.value?.size ?: 0
        }

        override fun onBindViewHolder(holder: ProductImgViewHolder, position: Int) {
            val imageUri = recyclerViewList.value?.get(position)
            if (imageUri != null) {
                // URI를 ImageView에 설정
                shopActivity.showServiceImage(
                    imageUri,
                    holder.rowProductInfoImgListBinding.rowImageViewProductInfoImg
                )
            }
        }
    }
}