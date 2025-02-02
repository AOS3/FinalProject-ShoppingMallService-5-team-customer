package com.judamie_user.android.ui.subfragment

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.judamie_user.android.R
import com.judamie_user.android.activity.ShopActivity
import com.judamie_user.android.databinding.FragmentShowUserOrderListBinding
import com.judamie_user.android.databinding.RowOrderListBinding
import com.judamie_user.android.firebase.model.OrderModel
import com.judamie_user.android.firebase.model.OrderPackageModel
import com.judamie_user.android.firebase.repository.OrderPackageRepository
import com.judamie_user.android.firebase.repository.OrderRepository
import com.judamie_user.android.firebase.service.OrderPackageService
import com.judamie_user.android.firebase.service.OrderService
import com.judamie_user.android.firebase.service.ProductService
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.util.OrderState
import com.judamie_user.android.util.tools.Companion.formatToComma
import com.judamie_user.android.util.tools.Companion.toFormattedDate
import com.judamie_user.android.viewmodel.fragmentviewmodel.ShowUserOrderListViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowOrderListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShowUserOrderListFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentShowUserOrderListBinding: FragmentShowUserOrderListBinding
    lateinit var shopActivity: ShopActivity

    //  주문목록RecyclerView를 구성하기 위해 사용할 리스트
    var gettingUserOrderPackageList = mutableListOf<OrderPackageModel>()

    // 주문 패키지에있는 모든 주문리스트가 담겨있는 리스트
    var wholeOrderModelList = mutableListOf(mutableListOf<OrderModel>())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShowUserOrderListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_show_user_order_list,
            container,
            false
        )
        fragmentShowUserOrderListBinding.showUserOrderListViewModel =
            ShowUserOrderListViewModel(this)
        fragmentShowUserOrderListBinding.lifecycleOwner = this

        shopActivity = activity as ShopActivity

        //스크롤 위에서 아래로잡아당겨서 업데이트
        refreshData()

        Log.d("test", gettingUserOrderPackageList.size.toString())

        // 해당유저의 주문목록을 가져온다
        if (gettingUserOrderPackageList.isEmpty()) {
            gettingUserOrderList()
        } else {
            settingRecyclerViewWishList()
            //fragmentShowUserOrderListBinding.recyclerViewShowUserOrderList.adapter?.notifyDataSetChanged()
        }



        return fragmentShowUserOrderListBinding.root
    }

    //스크롤 위에서 아래로잡아당겨서 업데이트
    private fun refreshData() {
        fragmentShowUserOrderListBinding.apply {
            swipeRefreshLayout.setOnRefreshListener {
                CoroutineScope(Dispatchers.Main).launch {
                    fragmentShowUserOrderListBinding.swipeRefreshLayout.isEnabled = false
                    swipeRefreshLayout.isRefreshing = true // 로딩 애니메이션 시작
                    showUserOrderListViewModel?.recyclerViewShowUserOrderListVisibility?.value =
                        View.INVISIBLE
                    swipeRefreshLayout.isRefreshing = false
                    try {
                        gettingUserOrderPackageList.clear()
                        gettingUserOrderList()
                    } catch (e: Exception) {
                        e.printStackTrace() // 예외 처리
                    } finally {
                        showUserOrderListViewModel?.recyclerViewShowUserOrderListVisibility?.value =
                            View.VISIBLE
                    }
                    fragmentShowUserOrderListBinding.swipeRefreshLayout.isEnabled = true
                }
            }
        }
    }

    fun movePrevFragment() {
        mainFragment.removeFragment(ShopSubFragmentName.SHOW_USER_ORDER_LIST_FRAGMENT)
    }

    // 해당유저의 주문목록을 가져온다
    fun gettingUserOrderList() {
        fragmentShowUserOrderListBinding.apply {
            CoroutineScope(Dispatchers.Main).launch {
                showUserOrderListViewModel?.progressBarShowUserOrderListVisibility?.value =
                    View.VISIBLE
                var work = async(Dispatchers.IO) {
                    OrderPackageService.gettingOrderPackageData(shopActivity.userDocumentID)
                }
                val result = work.await()
                if (result.isNotEmpty()) {
                    gettingUserOrderPackageList = result

                    //주문 목록 리사이클러뷰를 세팅한다
                    settingRecyclerViewWishList()

                    recyclerViewShowUserOrderList.adapter?.notifyDataSetChanged()
                } else {
                    showUserOrderListViewModel?.textViewNothingOrderVisibility?.value = View.VISIBLE
                }

            }
        }

    }

    //주문 목록 리사이클러뷰를 세팅한다
    fun settingRecyclerViewWishList() {
        fragmentShowUserOrderListBinding.apply {
            recyclerViewShowUserOrderList.adapter = RecyclerViewAdapter()
            recyclerViewShowUserOrderList.layoutManager = LinearLayoutManager(requireContext())

            val deco = MaterialDividerItemDecoration(
                requireContext(),
                MaterialDividerItemDecoration.VERTICAL
            )

            recyclerViewShowUserOrderList.addItemDecoration(deco)
        }
    }

    inner class RecyclerViewAdapter :
        RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {
        inner class RecyclerViewHolder(val rowOrderListBinding: RowOrderListBinding) :
            RecyclerView.ViewHolder(rowOrderListBinding.root) {
            init {
                rowOrderListBinding.root.setOnClickListener {
                    var dataBundle = Bundle()
                    dataBundle.putStringArrayList(
                        "orderDataList",
                        ArrayList(gettingUserOrderPackageList[adapterPosition].orderDataList)
                    )

                    mainFragment.replaceFragment(
                        ShopSubFragmentName.SHOW_USER_ORDER_INFO_FRAGMENT,
                        true,
                        true,
                        dataBundle
                    )
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
            val rowOrderListBinding = DataBindingUtil.inflate<RowOrderListBinding>(
                layoutInflater,
                R.layout.row_order_list,
                parent,
                false
            )

            rowOrderListBinding.rowOrderListViewModel =
                RowOrderListViewModel(this@ShowUserOrderListFragment)
            rowOrderListBinding.lifecycleOwner = viewLifecycleOwner

            return RecyclerViewHolder(rowOrderListBinding)
        }

        override fun getItemCount(): Int {
            return gettingUserOrderPackageList.size
        }

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            holder.rowOrderListBinding.rowOrderListViewModel?.apply {

                val orderModelList = mutableListOf<OrderModel>()
                var packageTotalPrice: Double = 0.0

                CoroutineScope(Dispatchers.Main).launch {
                    val orders = gettingUserOrderPackageList[position].orderDataList.map { orderId ->
                            async(Dispatchers.IO) { OrderService.gettingOrderData(orderId) }
                        }.awaitAll()

                    orderModelList.addAll(orders)
                    wholeOrderModelList.add(orderModelList)

                    // 총 가격 계산
                    packageTotalPrice = orderModelList.sumOf { it.orderPriceAmount }

                    // 대표 사진 가져오기
                    val firstOrder = orderModelList.firstOrNull()
                    firstOrder?.let { order ->
                        val productModel = withContext(Dispatchers.IO) {
                            ProductService.gettingProductOne(order.productDocumentId)
                        }
                        val mainUri = withContext(Dispatchers.IO) {
                            ProductService.gettingImage(productModel.productMainImage)
                        }

                        textViewOrderNameText?.value = productModel.productName
                        imageViewOrderImageUri?.value = mainUri
                    }

                    // "외 N개의 제품" 설정
                    textViewCountMoreText?.value =
                        if (orderModelList.size > 1) "외 ${orderModelList.size - 1}개의 제품" else ""

                    // 배송 상태 확인
                    val checkDeliveryState = orderModelList.map { it.orderState }
                    checkDeliveryState.forEach {
                        Log.d("checkDeliveryState", it.toString())
                    }

                    // 배송상태를 중복제하고 배송상태에따라 분기한다
                    checkDeliveryState.distinct()

                    // 모든 상품이 배달되지않음
                    if (checkDeliveryState.contains(OrderState.ORDER_STATE_PAYMENT_COMPLETE) && !checkDeliveryState.contains(
                            OrderState.ORDER_STATE_DELIVERY
                        )
                    ) {
                        textViewPickupState?.value = context?.getString(R.string.orderDone)//주문완료
                        textViewPickupStateColor?.value = R.color.gray
                    }
                    //배달된 상품도 있고 아닌상품도있음
                    if (checkDeliveryState.contains(OrderState.ORDER_STATE_PAYMENT_COMPLETE) && checkDeliveryState.contains(
                            OrderState.ORDER_STATE_DELIVERY
                        )
                    ) {
                        textViewPickupState?.value = context?.getString(R.string.onDelivery)//배송중
                        textViewPickupStateColor?.value = R.color.black
                    }
                    //모든 상품이 배송완료
                    if (!checkDeliveryState.contains(OrderState.ORDER_STATE_PAYMENT_COMPLETE) && checkDeliveryState.contains(
                            OrderState.ORDER_STATE_DELIVERY
                        )
                    ) {
                        textViewPickupState?.value = context?.getString(R.string.deliveryDone)//배송완료
                        textViewPickupStateColor?.value = R.color.deliveryDone
                    }
                    //모든 상품이 픽업완료&
                    if (checkDeliveryState.contains(OrderState.ORDER_STATE_PICKUP_COMPLETED) || checkDeliveryState.contains(
                            OrderState.ORDER_STATE_TRANSFER_COMPLETED)){
                        textViewPickupState?.value = context?.getString(R.string.pickupDone)//픽업완료
                        textViewPickupStateColor?.value = R.color.pickupDone
                    }

                    // 날짜 및 가격 설정
                    textViewOrderDateText?.value =
                        gettingUserOrderPackageList[position].orderPackageDataTimeStamp.toFormattedDate()
                    textViewTotalPrice?.value = "${packageTotalPrice.formatToComma()}"
                    textViewOrderCount?.value = "${orderModelList.size}개"

                    if (position == gettingUserOrderPackageList.size - 1) {
                        fragmentShowUserOrderListBinding.showUserOrderListViewModel?.apply {
                            progressBarShowUserOrderListVisibility?.value = View.GONE
                            recyclerViewShowUserOrderListVisibility?.value = View.VISIBLE
                        }
                    }
                }
            }
        }

    }


}
