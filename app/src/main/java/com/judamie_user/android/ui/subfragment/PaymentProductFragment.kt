package com.judamie_user.android.ui.subfragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.judamie_manager.firebase.model.CouponModel
import com.judamie_user.android.R
import com.judamie_user.android.activity.FragmentName
import com.judamie_user.android.activity.ShopActivity
import com.judamie_user.android.databinding.FragmentPaymentProductBinding
import com.judamie_user.android.databinding.RowPaymentProductListBinding
import com.judamie_user.android.firebase.model.OrderModel
import com.judamie_user.android.firebase.model.PickupLocationModel
import com.judamie_user.android.firebase.model.ProductModel
import com.judamie_user.android.firebase.model.UserModel
import com.judamie_user.android.firebase.service.CouponService
import com.judamie_user.android.firebase.service.OrderService
import com.judamie_user.android.firebase.service.PickupLocationService
import com.judamie_user.android.firebase.service.ProductService
import com.judamie_user.android.firebase.service.UserService
import com.judamie_user.android.ui.fragment.CartItem
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.util.CouponUsableType
import com.judamie_user.android.util.OrderState
import com.judamie_user.android.util.tools.Companion.formatToComma
import com.judamie_user.android.viewmodel.fragmentviewmodel.PaymentProductViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowPaymentProductListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.times

class PaymentProductFragment(val mainFragment: MainFragment) : Fragment() {

    private lateinit var fragmentPaymentProductBinding: FragmentPaymentProductBinding
    private lateinit var shopActivity: ShopActivity

    // 선택된 쿠폰의 Index를 저장
    private var selectedCouponIndex: Int = 0

//    // ReyclerView 구성을 위한 임시 데이터
//    val tempList1 = Array(5) {
//        "조니워커 블루라벨 뱀띠 에디션 750ml"
//    }

    // 상품 목록 RecyclerView 구성을 위한 데이터
    private val recyclerViewPaymentList = mutableListOf<Pair<ProductModel, Int>>()

    // 사용자 정보를 담을 변수
    private lateinit var userModel : UserModel

    private lateinit var pickupModel : PickupLocationModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentPaymentProductBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_payment_product, container, false)
        fragmentPaymentProductBinding.paymentProductViewModel = PaymentProductViewModel(this@PaymentProductFragment)
        fragmentPaymentProductBinding.lifecycleOwner = this@PaymentProductFragment

        shopActivity = activity as ShopActivity

        fragmentPaymentProductBinding.paymentProductViewModel?.buttonPaymentSelectCouponText?.value = "적용할 쿠폰을 선택해주세요."

        // 툴바 구성 메서드 호출
        settingToolbar()
        // 사용자 정보를 가져온다.
        settingUserInfo()
        // 결제 상품 목록 RecyclerView 구성 메서드 호출
        settingPaymentRecyclerView()
        // 결제 상품 목록을 갱신하는 메서드
        refreshPaymentRecyclerView()

        return fragmentPaymentProductBinding.root
    }


    // 툴바 구성 메서드
    fun settingToolbar(){
        fragmentPaymentProductBinding.paymentProductViewModel?.apply {
            // 타이틀
            toolbarPaymentProductTitle.value = "결제하기"
            // 네비게이션 아이콘
            toolbarPaymentProductNavigationIcon.value = R.drawable.arrow_back_ios_24px
        }
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment() {
        // 다이얼로그 생성
        val dialogBuilder = AlertDialog.Builder(mainFragment.requireContext())
        dialogBuilder.setMessage("주문을 취소하고 나가시겠어요?")
            .setCancelable(false)
            .setPositiveButton("확인") { dialog, _ ->
                // 확인 버튼 클릭 시 이전 Fragment 제거
                mainFragment.removeFragment(ShopSubFragmentName.PAYMENT_PRODUCT_FRAGMENT)
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                // 취소 버튼 클릭 시 다이얼로그 닫기
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("결제 취소")
        alert.show()
    }

    // 결제 처리 메서드
    fun proPayment() {
        fragmentPaymentProductBinding.apply {
            CoroutineScope(Dispatchers.Main).launch {
                // 상품별 주문 데이터 생성
                recyclerViewPaymentList.forEach { (product, count) ->
                    // productID를 통해 상품 정보 가져오기
                    val productInfo = async(Dispatchers.IO) {
                        ProductService.selectProductDataOneById(product.productDocumentId ?: "")
                    }
                    val productModel = productInfo.await()

                    // 필요한 데이터 설정
                    val userDocumentId = shopActivity.userDocumentID
                    val sellerDocumentId = productModel.productSeller ?: ""
                    val orderTime = System.nanoTime()
                    val productDocumentId = productModel.productDocumentId ?: ""
                    val productPrice = productModel.productPrice ?: 0
                    val productDiscountRate = productModel.productDiscountRate ?: 0
                    val orderCount = count
                    val pickupLocDocumentId = userModel.userPickupLoc
                    val orderState = OrderState.ORDER_STATE_PAYMENT_COMPLETE
                    val orderTimeStamp = System.nanoTime()

                    // 할인율을 적용한 주문 가격 계산
                    val orderPriceAmount = ((productPrice * (1 - (productDiscountRate.toDouble() / 100.0))) * orderCount).toInt()

                    // OrderModel 생성
                    val orderModel = OrderModel().also {
                        it.userDocumentId = userDocumentId
                        it.pickupLocDocumentId = pickupLocDocumentId
                        it.sellerDocumentID = sellerDocumentId
                        it.productDocumentId = productDocumentId
                        it.productPrice = productPrice
                        it.productDiscountRate = productDiscountRate
                        it.orderCount = orderCount
                        it.orderTime = orderTime
                        it.orderState = orderState
                        it.orderPriceAmount = orderPriceAmount.toDouble()
                        it.orderTimeStamp = orderTimeStamp
                    }

                    // 서버에 저장
                    CoroutineScope(Dispatchers.Main).launch {
                        // 프로그래스바를 보이게 한다
                        progressBarPaymentProduct.visibility = View.VISIBLE

                        val work1 = async(Dispatchers.IO) {
                            // 서비스의 저장 메서드를 호출한다.
                            OrderService.addOrderData(orderModel)
                        }
                        work1.await()

                        // 프로그래스바 숨기기
                        progressBarPaymentProduct.visibility = View.GONE

                        // 결제 확인 화면으로 이동
                        mainFragment.replaceFragment(
                            ShopSubFragmentName.COMPLETE_PAYMENT_FRAGMENT,
                            true,
                            true,
                            null
                        )
                    }
                }
            }

            // TODO
            // 결제 API 연동 작업
        }
    }


    // 유저의 정보를 가져오는 메서드
    fun settingUserInfo() {
        CoroutineScope(Dispatchers.Main).launch {
            // 유저 정보 가져오기
            val work1 = async(Dispatchers.IO) {
                UserService.selectUserDataByUserDocumentIdOne(shopActivity.userDocumentID)
            }
            userModel = work1.await()

            // 유저 픽업지 정보 가져오기
            val userPickupLoc = userModel.userPickupLoc
            //Log.d("test100", "설정된 유저 픽업지: $userPickupLoc")

            val work2 = async(Dispatchers.IO) {
                PickupLocationService.gettingPickupLocationById(userPickupLoc)
            }
            pickupModel = work2.await()

            //Log.d("test100", "픽업지 ID: $pickupModel")

            // 뷰에 데이터 반영
            fragmentPaymentProductBinding.apply {
                paymentProductViewModel?.apply {
                    textViewPaymentUserNameText?.value = userModel.userName
                    textViewPaymentUserNoText?.value = userModel.userPhoneNumber
                    textViewPaymentPickupLocNameText?.value = pickupModel.pickupLocName
                    textViewPaymentPickupLocAddressInfoText?.value =
                        "${pickupModel.pickupLocStreetAddress}" + "${pickupModel.pickupLocAddressDetail}"
                }
            }
        }
    }

    // 상품 목록을 갱신하는 메서드
    private fun refreshPaymentRecyclerView() {

        val selectedItemList = arguments?.getParcelableArrayList<CartItem>("selectedProducts")

        CoroutineScope(Dispatchers.Main).launch {
            // 상품 정보 - 수량 매핑
            val productsWithCounts = selectedItemList?.map { cartItem ->
                async(Dispatchers.IO) {
                    val product = ProductService.selectProductDataOneById(cartItem.productId)
                    product to cartItem.count
                }
            }?.awaitAll()

            recyclerViewPaymentList.clear()
            productsWithCounts?.let { recyclerViewPaymentList.addAll(it) }

            fragmentPaymentProductBinding.recyclerViewPaymentProduct.adapter?.notifyDataSetChanged()

            // 결제 금액 계산 메서드 호출
            settingTotalPrice()

            fragmentPaymentProductBinding.paymentProductViewModel?.textViewPaymentProductCountText?.value =
                "${recyclerViewPaymentList.size}건"
        }
    }

    // 결제 상품 RecyclerView 구성 메서드
    fun settingPaymentRecyclerView(){
        fragmentPaymentProductBinding.apply {
            recyclerViewPaymentProduct.adapter = PaymentRecyclerViewAdapter()
            recyclerViewPaymentProduct.layoutManager = LinearLayoutManager(shopActivity)
        }
    }

    // 결제 상품 RecyclerView의 어뎁터
    inner class PaymentRecyclerViewAdapter : RecyclerView.Adapter<PaymentRecyclerViewAdapter.PaymentViewHolder>(){
        inner class PaymentViewHolder(val rowPaymentProductListBinding: RowPaymentProductListBinding) : RecyclerView.ViewHolder(rowPaymentProductListBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
            val rowPaymentProductListBinding = DataBindingUtil.inflate<RowPaymentProductListBinding>(layoutInflater, R.layout.row_payment_product_list, parent, false)
            rowPaymentProductListBinding.rowPaymentProductListViewModel = RowPaymentProductListViewModel(this@PaymentProductFragment)
            rowPaymentProductListBinding.lifecycleOwner = this@PaymentProductFragment

            val paymentViewHolder = PaymentViewHolder(rowPaymentProductListBinding)

            return paymentViewHolder
        }

        override fun getItemCount(): Int {
            return recyclerViewPaymentList.size
        }

        override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {

            val (product, count) = recyclerViewPaymentList[position]

            holder.rowPaymentProductListBinding.rowPaymentProductListViewModel?.textViewCartProductNameText?.value =
                product.productName
            holder.rowPaymentProductListBinding.rowPaymentProductListViewModel?.rowTextViewPaymentProductPriceText?.value =
                "${product.productPrice.formatToComma()}"
            holder.rowPaymentProductListBinding.rowPaymentProductListViewModel?.rowTextViewPaymentProductCountText?.value = "${count}개"
            holder.rowPaymentProductListBinding.rowPaymentProductListViewModel?.rowTextViewPaymentStoreNameText?.value = product.productSeller

            // 할인률
            val discount = product.productDiscountRate
            holder.rowPaymentProductListBinding.rowPaymentProductListViewModel?.rowTextViewPaymentProductPercentText?.value =
                if (discount > 0) "${discount}%" else ""
            holder.rowPaymentProductListBinding.rowTextViewPaymentProductPercent.visibility =
                if (discount > 0) View.VISIBLE else View.GONE

            // 썸네일 이미지
            val imageUrl = product.productMainImage // 현재 항목의 이미지 URL

            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val imageUrl = withContext(Dispatchers.IO) {
                        ProductService.gettingImage(imageUrl)
                    }

                    // Glide로 이미지 로드
                    Glide.with(holder.rowPaymentProductListBinding.root.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.liquor_24px)
                        .error(R.drawable.liquor_24px)
                        .into(holder.rowPaymentProductListBinding.imageViewPaymentProduct)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }


    // 유저 쿠폰 정보를 불러오는 메서드
    fun settingUserCoupons() {
        // 유저 데이터를 받아온다.
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val work1 = async(Dispatchers.IO) {
                    UserService.selectUserDataByUserDocumentIdOne(shopActivity.userDocumentID)
                }
                userModel = work1.await()
                val userCouponList = userModel.userCoupons
                // 쿠폰 데이터를 가져온다.
                val work2 = async(Dispatchers.IO) {
                    CouponService.gettingCouponList(userCouponList)
                }
                val couponList = work2.await()

                // TODO
                // 쿠폰 날짜가 만료되면 안보이도록 작업

                // CouponUsableType이 2가 아닌 쿠폰만 필터링
                val filteredCouponList = couponList.filter { coupon ->
                    coupon.couponState != CouponUsableType.fromNumber(2)
                }
                // 다이얼로그 표시
                settingCouponDialog(filteredCouponList)
            } catch (e:Exception) {

            }
            // Coupon 목록 값 확인
            //Log.d("test100", "User Coupon List: ${userCouponList}")
        }
    }

    // 쿠폰 선택 다이얼로그 표시
    fun settingCouponDialog(couponList: List<CouponModel>) {
        // setSingleChoiceItems는 Array<String> ListAdapter만 허용
        // ArrayList<String>는 바로 사용 불가
        val userCouponList = mutableListOf("쿠폰 적용 안함").apply {
            addAll(couponList.map { coupon ->
                "${coupon.couponName}\n" +
                        "${coupon.couponDiscountRate}% 할인\n" +
                        "사용 기한: ${coupon.couponPeriod}까지"
            })
        }

        // AlertDialog 생성
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("적용할 쿠폰을 선택해주세요")
        alertDialogBuilder.setSingleChoiceItems(userCouponList.toTypedArray(), selectedCouponIndex) { _, which ->
            // 선택된 쿠폰 Index 저장
            selectedCouponIndex = which
        }
        alertDialogBuilder.setPositiveButton("쿠폰 선택") { dialog, _ ->
            // 쿠폰 적용 안함을 선택했을 경우
            if (selectedCouponIndex == 0) {
                // 쿠폰 처리
                fragmentPaymentProductBinding.paymentProductViewModel?.buttonPaymentSelectCouponText?.value = "쿠폰 적용 안함"
            } else {
                // 특정 쿠폰을 선택했을 경우
                // 쿠폰 적용 안함 제외
                val selectedCoupon = couponList[selectedCouponIndex - 1]
                fragmentPaymentProductBinding.paymentProductViewModel?.buttonPaymentSelectCouponText?.value = "${selectedCoupon.couponName}/${selectedCoupon.couponDiscountRate}% 할인"

                // 쿠폰 금액 계산
                settingTotalPrice(selectedCoupon)
            }
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("취소") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.show()

    }

    // 총 결제 금액을 보여주는 메서드
    fun settingTotalPrice(selectedCoupon: CouponModel? = null) {
        // 총 상품 가격
        val totalProductPrice = recyclerViewPaymentList.sumOf { (product, count) ->
            product.productPrice * count
        }
        // 쿠폰 할인 금액
        val couponDiscountRate = selectedCoupon?.couponDiscountRate?.toIntOrNull() ?: 0
        val discountCouponAmount = totalProductPrice * couponDiscountRate/100
        // 최종 결제 금액
        val totalPaymentPrice = totalProductPrice - discountCouponAmount

        fragmentPaymentProductBinding.paymentProductViewModel?.textViewPaymentAllProductPriceText?.value = "${totalProductPrice.formatToComma()}"
        fragmentPaymentProductBinding.paymentProductViewModel?.textViewPaymentCouponDiscountPriceText?.value = if (couponDiscountRate > 0) {
            // 쿠폰 적용
            "- ${discountCouponAmount.formatToComma()}"
        } else {
            // 쿠폰 미적용
            "0원"
        }
        fragmentPaymentProductBinding.paymentProductViewModel?.textViewPaymentTotalPriceText?.value = "${totalPaymentPrice.formatToComma()}"

        fragmentPaymentProductBinding.paymentProductViewModel?.buttonPaymentPayText?.value = "${totalPaymentPrice.formatToComma()} 결제하기"

    }

}