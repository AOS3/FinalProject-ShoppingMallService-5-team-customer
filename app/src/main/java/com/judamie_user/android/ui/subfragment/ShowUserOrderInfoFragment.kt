package com.judamie_user.android.ui.subfragment

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentShowUserOrderInfoBinding
import com.judamie_user.android.databinding.RowOrderInfoBinding
import com.judamie_user.android.firebase.model.OrderModel
import com.judamie_user.android.firebase.service.OrderService
import com.judamie_user.android.firebase.service.PickupLocationService
import com.judamie_user.android.firebase.service.ProductService
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.util.OrderState
import com.judamie_user.android.util.tools.Companion.formatToComma
import com.judamie_user.android.util.tools.Companion.toFormattedDate
import com.judamie_user.android.viewmodel.fragmentviewmodel.ShowUserOrderInfoViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowOrderInfoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class ShowUserOrderInfoFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentShowUserOrderInfoBinding: FragmentShowUserOrderInfoBinding

    // 주문 상세 리스트를 담을 임시데이터
    var recyclerViewShowUserOrderInfoList = Array(5, {
        "항목 ${it + 1}"
    })

    var orderPackageDocumentID = ""

    var orderDataList = mutableListOf<String>()

    var wholeOrderModelList = mutableListOf<OrderModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShowUserOrderInfoBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_show_user_order_info,container,false)
        fragmentShowUserOrderInfoBinding.showUserOrderInfoViewModel = ShowUserOrderInfoViewModel(this)
        fragmentShowUserOrderInfoBinding.lifecycleOwner = viewLifecycleOwner

        //리사이클러뷰세팅
        //settingRecyclerView()

//        if(orderDataList.isEmpty()){
//            gettingOrderModelList()
//        }else{
//            //리사이클러뷰를 다시표시함
//            settingRecyclerView()
//        }

        orderDataList.clear()
        wholeOrderModelList.clear()
        gettingOrderModelList()


        return fragmentShowUserOrderInfoBinding.root
    }

    fun gettingOrderModelList(){
        CoroutineScope(Dispatchers.Main).launch {

            // orderDataList를 받아온다
            gettingOrderDataList()

            orderDataList.forEach {
                val work1 = async (Dispatchers.IO) {
                    OrderService.gettingOrderData(it)
                }
                wholeOrderModelList.add(work1.await())
            }
            wholeOrderModelList.forEach {
                Log.d("wholeOrderModelList",it.productDocumentId)
            }

            //해당주문의 픽업지를 가져온다
            val work2 = async (Dispatchers.IO){
                PickupLocationService.gettingPickupLocationById(wholeOrderModelList.first().pickupLocDocumentId)
            }
            val pickUpLocationModel = work2.await()


            fragmentShowUserOrderInfoBinding.showUserOrderInfoViewModel?.apply {
                // 주문 날짜 설정
                textViewShowUserOrderInfoDateText?.value =
                    wholeOrderModelList.first().orderTime.toFormattedDate()

                // 픽업지 설정(도로명 주소)
                textViewShowUserOrderInfoPickupLocationStreetAddressText?.value =
                    pickUpLocationModel.pickupLocStreetAddress

                // 픽업지 설정(상세 주소)
                textViewShowUserOrderInfoPickupLocationAddressDetailText?.value =
                    pickUpLocationModel.pickupLocAddressDetail

                //모든 주문의 진행상태를 가져온다 // 배송 상태 확인
                val checkDeliveryState = wholeOrderModelList.map { it.orderState }
                checkDeliveryState.forEach {
                    Log.d("checkDeliveryState", it.toString())
                }
                // 배송상태를 중복제거하고 배송상태에따라 분기한다
                checkDeliveryState.distinct()
                buttonShowUserOrderInfoPickupDoneEnabled?.value = true

                Log.d("test",checkDeliveryState.toString())

                if (checkDeliveryState.contains(OrderState.ORDER_STATE_PAYMENT_COMPLETE)){
                    buttonShowUserOrderInfoPickupDoneEnabled?.value = false
                    buttonShowUserOrderInfoPickupDoneBackground?.value = ContextCompat.getDrawable(requireContext(), R.drawable.button_round_deactivated)
                    buttonShowUserOrderInfoPickupDoneTextColor?.value = Color.GRAY
                }
                if (!checkDeliveryState.contains(OrderState.ORDER_STATE_PAYMENT_COMPLETE) &&
                    checkDeliveryState.contains(OrderState.ORDER_STATE_DELIVERY)){
                    buttonShowUserOrderInfoPickupDoneEnabled?.value = true
                    buttonShowUserOrderInfoPickupDoneBackground?.value = ContextCompat.getDrawable(requireContext(), R.drawable.button_round_activated)
                    buttonShowUserOrderInfoPickupDoneTextColor?.value = Color.WHITE
                }
                if (checkDeliveryState.contains(OrderState.ORDER_STATE_PICKUP_COMPLETED)||
                    checkDeliveryState.contains(OrderState.ORDER_STATE_TRANSFER_COMPLETED)){
                    buttonShowUserOrderInfoPickupDoneEnabled?.value = false
                    buttonShowUserOrderInfoPickupDoneBackground?.value = ContextCompat.getDrawable(requireContext(), R.drawable.button_round_deactivated)
                    buttonShowUserOrderInfoPickupDoneTextColor?.value = Color.GRAY
                }

                Log.d("test",buttonShowUserOrderInfoPickupDoneEnabled.value.toString())

            }
            settingRecyclerView()

        }
    }

    //픽업완료버튼의 동작을 구성한다
    fun settingButtonShowUserOrderInfoPickupDone(){

        val dialogView = layoutInflater.inflate(R.layout.dialog_pickupdone, null)

        val buttonPickupCancel = dialogView.findViewById<Button>(R.id.buttonPickupCancel)
        val buttonPickupDone = dialogView.findViewById<Button>(R.id.buttonPickupDone)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        //취소
        buttonPickupCancel.setOnClickListener {
            dialog.dismiss()
        }

        // 픽업처리
        buttonPickupDone.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                wholeOrderModelList.forEach {
                    val work1 = async (Dispatchers.IO){
                        OrderService.updateOrderData(it.orderDocumentId,OrderState.ORDER_STATE_PICKUP_COMPLETED)
                    }
                    work1.await()
                }

                wholeOrderModelList.clear()
                gettingOrderModelList()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    // orderDataList를 받아온다
    fun gettingOrderDataList(){
        orderDataList = arguments?.getStringArrayList("orderDataList")!!.toMutableList()
        orderPackageDocumentID = arguments?.getString("orderPackageDocumentID")!!
    }



    fun movePrevFragment() {
        mainFragment.removeFragment(ShopSubFragmentName.SHOW_USER_ORDER_INFO_FRAGMENT)
    }

    fun settingRecyclerView() {
        fragmentShowUserOrderInfoBinding.apply {
            recyclerViewShowUserOrderInfo.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewShowUserOrderInfo.adapter = RecyclerViewAdapter()
            val horizontalDivider = MaterialDividerItemDecoration(
                requireContext(),
                MaterialDividerItemDecoration.HORIZONTAL
            )
            recyclerViewShowUserOrderInfo.addItemDecoration(horizontalDivider)

        }
    }

    inner class RecyclerViewAdapter():RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){
        inner class ViewHolder(val rowOrderInfoBinding: RowOrderInfoBinding):RecyclerView.ViewHolder(rowOrderInfoBinding.root){
            init {
                rowOrderInfoBinding.buttonRowOrderInfoWriteReview.setOnClickListener {
                    if (wholeOrderModelList[adapterPosition].reviewDocumentID !=""){
                        //리뷰 보기로 이동한다
                        val dataBundle = Bundle()
                        dataBundle.putString("productDocumentId",wholeOrderModelList[adapterPosition].productDocumentId)
                        mainFragment.replaceFragment(ShopSubFragmentName.PRODUCT_REVIEW_LIST_FRAGMENT,true,true,dataBundle)
                    }else{
                        //리뷰작성페이지로 이동한다
                        val dataBundle = Bundle()
                        dataBundle.putString("productDocumentID",wholeOrderModelList[adapterPosition].productDocumentId)
                        dataBundle.putString("orderDocumentID",wholeOrderModelList[adapterPosition].orderDocumentId)
                        mainFragment.replaceFragment(ShopSubFragmentName.WRITE_PRODUCT_REVIEW_FRAGMENT,true,true,dataBundle)
                    }

                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val rowOrderInfoBinding = DataBindingUtil.inflate<RowOrderInfoBinding>(
                layoutInflater, R.layout.row_order_info,parent,false
            )
            rowOrderInfoBinding.rowOrderInfoViewModel = RowOrderInfoViewModel(this@ShowUserOrderInfoFragment)
            rowOrderInfoBinding.lifecycleOwner = viewLifecycleOwner

            return ViewHolder(rowOrderInfoBinding)
        }

        override fun getItemCount(): Int {
            return wholeOrderModelList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            var imageUri:Uri

            holder.rowOrderInfoBinding.rowOrderInfoViewModel?.apply {
                CoroutineScope(Dispatchers.Main).launch {
                    val work1 = async (Dispatchers.IO){
                        ProductService.gettingProductOne(wholeOrderModelList[position].productDocumentId)
                    }
                    val productModel = work1.await()

                    val work2 = async (Dispatchers.IO){
                        ProductService.gettingImage(productModel.productMainImage)
                    }
                    imageUri = work2.await()

                    imageViewRowOrderInfoProductImageUri?.value = imageUri

                    textViewRowOrderInfoProductNameText?.value = productModel.productName

                    textViewRowOrderInfoProductPriceText?.value = wholeOrderModelList[position].orderPriceAmount.formatToComma()

                    textViewRowOrderInfoProductCountText?.value = "${wholeOrderModelList[position].orderCount}개"

                    textViewRowOrderInfoProductSellerText?.value = productModel.productSeller

                    textViewRowOrderInfoProductState?.value = when(wholeOrderModelList[position].orderState){
                        OrderState.ORDER_STATE_PAYMENT_COMPLETE -> getString(R.string.orderDone)
                        OrderState.ORDER_STATE_DELIVERY -> getString(R.string.deliveryDone)
                        OrderState.ORDER_STATE_PICKUP_COMPLETED -> ""
                        OrderState.ORDER_STATE_TRANSFER_COMPLETED -> ""
                    }.toString()

                    //해당주문에대해 리뷰가 이미있다면 리뷰작성 버튼을 리뷰 보기버튼으로 변경한다
                    if (wholeOrderModelList[position].reviewDocumentID !="") {
                        buttonRowOrderInfoWriteReviewBackground?.value = ContextCompat.getDrawable(context!!, R.drawable.button_round_activated)
                        buttonRowOrderInfoWriteReviewEnabled?.value = true
                        buttonRowOrderInfoWriteReviewTextColor?.value = Color.WHITE
                        buttonRowOrderInfoWriteReviewText?.value = "리뷰보기"
                    }else{
                        if (wholeOrderModelList[position].orderState == OrderState.ORDER_STATE_PICKUP_COMPLETED ||
                            wholeOrderModelList[position].orderState == OrderState.ORDER_STATE_TRANSFER_COMPLETED){
                            buttonRowOrderInfoWriteReviewBackground?.value = ContextCompat.getDrawable(context!!, R.drawable.button_round_activated)
                            buttonRowOrderInfoWriteReviewEnabled?.value = true
                            buttonRowOrderInfoWriteReviewTextColor?.value = Color.WHITE
                        }else{
                            buttonRowOrderInfoWriteReviewBackground?.value = ContextCompat.getDrawable(context!!, R.drawable.button_round_deactivated)
                            buttonRowOrderInfoWriteReviewEnabled?.value = false
                            buttonRowOrderInfoWriteReviewTextColor?.value = Color.GRAY
                        }
                    }
                }
            }
        }
    }


}