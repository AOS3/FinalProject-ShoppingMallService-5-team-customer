package com.judamie_user.android.ui.subfragment

import android.app.AlertDialog
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
import com.judamie_user.android.databinding.FragmentPaymentProductBinding
import com.judamie_user.android.databinding.RowPaymentProductListBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.viewmodel.fragmentviewmodel.PaymentProductViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowPaymentProductListViewModel

class PaymentProductFragment(val mainFragment: MainFragment) : Fragment() {

    private lateinit var fragmentPaymentProductBinding: FragmentPaymentProductBinding
    private lateinit var shopActivity: ShopActivity

    // ReyclerView 구성을 위한 임시 데이터
    val tempList1 = Array(5) {
        "조니워커 블루라벨 뱀띠 에디션 750ml"
    }

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
        // 장바구니 RecyclerView 구성 메서드 호출
        settingCartRecyclerView()

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

    }

    // 장바구니 RecyclerView 구성 메서드
    fun settingCartRecyclerView(){
        fragmentPaymentProductBinding.apply {
            recyclerViewPaymentProduct.adapter = PaymentRecyclerViewAdapter()
            recyclerViewPaymentProduct.layoutManager = LinearLayoutManager(shopActivity)
            val deco = MaterialDividerItemDecoration(shopActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewPaymentProduct.addItemDecoration(deco)
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
            return tempList1.size
        }

        override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
            holder.rowPaymentProductListBinding.rowPaymentProductListViewModel?.textViewCartProductNameText?.value = tempList1[position]
        }
    }

    // 쿠폰 선택 메서드
    fun selectCoupon() {
        // 쿠폰 데이터
        val couponList = arrayListOf("쿠폰 적용 안함","10% 할인쿠폰", "3% 할인쿠폰", "50% 할인쿠폰")
        // setSingleChoiceItems는 Array<String> ListAdapter만 허용
        // ArrayList<String>는 바로 사용 불가
        val couponListArray = couponList.toTypedArray()
        var selectedCouponIndex = 0

        // AlertDialog
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("쿠폰 선택")
        alertDialogBuilder.setSingleChoiceItems(couponListArray, selectedCouponIndex) { _, which ->
            // 선택된 쿠폰 Index 저장
            selectedCouponIndex = which
        }
        alertDialogBuilder.setPositiveButton("쿠폰 선택") { dialog, _ ->
            if (selectedCouponIndex != 0) {
                val selectedCoupon = couponList[selectedCouponIndex]
                // 쿠폰 처리하기
                fragmentPaymentProductBinding.paymentProductViewModel?.buttonPaymentSelectCouponText?.value = selectedCoupon
            } else {
                // 선택하지 않았을 경우
            }
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("취소") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.show()

    }

}