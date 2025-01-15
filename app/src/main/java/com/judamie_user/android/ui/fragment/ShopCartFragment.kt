package com.judamie_user.android.ui.fragment

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
import com.judamie_user.android.databinding.FragmentModifyUserInfoBinding
import com.judamie_user.android.databinding.FragmentShopCartBinding
import com.judamie_user.android.databinding.RowCartProductListBinding
import com.judamie_user.android.viewmodel.fragmentviewmodel.ShopCartViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowCartProductListViewModel

class ShopCartFragment(val mainFragment: MainFragment) : Fragment() {

    private lateinit var fragmentShopCartBinding: FragmentShopCartBinding
    private lateinit var shopActivity: ShopActivity

    // ReyclerView 구성을 위한 임시 데이터
    val tempList1 = Array(20) {
        "발베니 14년산"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShopCartBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_shop_cart, container, false)
        fragmentShopCartBinding.shopCartViewModel = ShopCartViewModel(this@ShopCartFragment)
        fragmentShopCartBinding.lifecycleOwner = this@ShopCartFragment

        shopActivity = activity as ShopActivity

        // 툴바 구성 메서드 호출
        settingToolbar()
        // 장바구니 RecyclerView 구성 메서드 호출
        settingCartRecyclerView()

        // 장바구니가 비었을 때 텍스트와 버튼을 안보이는 상태로 설정
        // fragmentShopCartBinding.textViewEmptyCart.isVisible = false
        // fragmentShopCartBinding.buttonEmptyCart.isVisible = false

        return fragmentShopCartBinding.root
    }

    // 툴바 구성 메서드
    fun settingToolbar(){
        fragmentShopCartBinding.shopCartViewModel?.apply {
            // 타이틀
            toolbarShopCartTitle.value = "장바구니"
            // 네비게이션 아이콘
            toolbarShopCartNavigationIcon.value = R.drawable.arrow_back_ios_24px
        }
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment(){
        mainFragment.removeFragment(ShopSubFragmentName.SHOP_CART_FRAGMENT)
    }

    // 다음 결제 화면 이동 메서드
    fun moveToPaymentProduct(){
        fragmentShopCartBinding.apply {
            // 사용자가 체크한 목록을 가져온다.

            // 데이터를 담는다.
            // val dataBundle = Bundle()
            // dataBundle.putString("", )
            mainFragment.replaceFragment(ShopSubFragmentName.PAYMENT_PRODUCT_FRAGMENT, true, true, null)
        }
    }




    // 장바구니 품목 삭제 처리 메서드
    fun deleteCartList() {

    }

    // 장바구니 목록 갱신 메서드
    fun refreshCartRecyclerView() {

    }

    // 장바구니 RecyclerView 구성 메서드
    fun settingCartRecyclerView(){
        fragmentShopCartBinding.apply {
            recyclerViewShopCart.adapter = CartRecyclerViewAdapter()
            recyclerViewShopCart.layoutManager = LinearLayoutManager(shopActivity)
            val deco = MaterialDividerItemDecoration(shopActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewShopCart.addItemDecoration(deco)
        }
    }

    // 장바구니 RecyclerView의 어뎁터
    inner class CartRecyclerViewAdapter : RecyclerView.Adapter<CartRecyclerViewAdapter.CartViewHolder>(){
        inner class CartViewHolder(val rowCartProductListBinding: RowCartProductListBinding) : RecyclerView.ViewHolder(rowCartProductListBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
            val rowCartProductListBinding = DataBindingUtil.inflate<RowCartProductListBinding>(layoutInflater, R.layout.row_cart_product_list, parent, false)
            rowCartProductListBinding.rowCartProductListViewModel = RowCartProductListViewModel(this@ShopCartFragment)
            rowCartProductListBinding.lifecycleOwner = this@ShopCartFragment

            val cartViewHolder = CartViewHolder(rowCartProductListBinding)

            rowCartProductListBinding.root.setOnClickListener {
                // 사용자가 누른 항목의 게시글 문서 번호를 담아서 전달
//                val dataBundle = Bundle()
//                dataBundle.putString("boardDocumentId", recyclerViewList[mainViewHolder.adapterPosition].boardDocumentId)
//                boardMainFragment.replaceFragment(BoardSubFragmentName.BOARD_READ_FRAGMENT, true, true, dataBundle)
            }

            return cartViewHolder
        }

        override fun getItemCount(): Int {
            return tempList1.size
        }

        override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
            holder.rowCartProductListBinding.rowCartProductListViewModel?.textViewCartProductNameText?.value = tempList1[position]
            holder.rowCartProductListBinding.rowCartProductListViewModel?.textViewCartProductPriceText?.value = tempList1[position]
            holder.rowCartProductListBinding.rowCartProductListViewModel?.textViewCartProductPercentText?.value = tempList1[position]

        }
    }
}