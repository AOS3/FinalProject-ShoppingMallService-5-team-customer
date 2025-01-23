package com.judamie_user.android.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.judamie_user.android.R
import com.judamie_user.android.activity.ShopActivity
import com.judamie_user.android.databinding.FragmentShopCartBinding
import com.judamie_user.android.databinding.RowCartProductListBinding
import com.judamie_user.android.firebase.model.ProductModel
import com.judamie_user.android.firebase.model.UserModel
import com.judamie_user.android.firebase.service.ProductService
import com.judamie_user.android.firebase.service.UserService
import com.judamie_user.android.viewmodel.fragmentviewmodel.ShopCartViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowCartProductListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class ShopCartFragment(val mainFragment: MainFragment) : Fragment() {

    private lateinit var fragmentShopCartBinding: FragmentShopCartBinding
    private lateinit var shopActivity: ShopActivity

    // 리사이클러뷰 구성 리스트
    var recyclerViewList = mutableListOf<ProductModel>()
    // 사용자 정보를 담을 변수
    lateinit var userModel: UserModel
    // 수량 정보
    var productCountList = MutableLiveData<MutableList<Int>>()

    var imageUris = MutableLiveData<MutableList<Uri>>()

    // RecyclerView CheckBox 상태 관리
    val checkBoxStates = MutableLiveData<MutableList<Boolean>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShopCartBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_shop_cart, container, false)
        fragmentShopCartBinding.shopCartViewModel = ShopCartViewModel(this@ShopCartFragment)
        fragmentShopCartBinding.lifecycleOwner = this@ShopCartFragment


        shopActivity = activity as ShopActivity

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Log.e("AuthError", "User is not logged in")
        } else {
            Log.d("Auth", "User is logged in: ${user.uid}")
        }


        // RecyclerView 구성을 위한 리스트를 초기화한다.
        recyclerViewList.clear()

        // 툴바 구성 메서드 호출
        settingToolbar()
        // 장바구니 RecyclerView 구성 메서드 호출
        settingCartRecyclerView()
        // 데이터를 가져와 RecyclerView를 갱신하는 메서드를 호출한다.
        loadCartProducts()

        // 장바구니가 비었을 때 텍스트와 버튼을 안보이는 상태로 설정
        // fragmentShopCartBinding.textViewEmptyCart.isVisible = false
        // fragmentShopCartBinding.buttonEmptyCart.isVisible = false

        return fragmentShopCartBinding.root
    }

    // 로그인한 유저의 장바구니 상품 리스트 가져오기(리사이클러뷰 갱신)
    fun loadCartProducts() {
        // 로그인한 유저 정보 가져오기
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                UserService.selectUserDataByUserDocumentIdOne(shopActivity.userDocumentID)
            }
            userModel = work1.await()
            val userCartList = userModel.userCartList


            // CartList에 해당하는 상품 정보 리스트 가져오기
            val work2 = async(Dispatchers.IO) {
                ProductService.gettingCartList(userCartList)
            }
            var productList = work2.await()

            // 장바구니가 비어있는 경우
            if (productList.isEmpty()) {
                fragmentShopCartBinding.textViewShopCartEmpty.visibility = View.VISIBLE
                fragmentShopCartBinding.buttonShopCartEmpty.visibility = View.VISIBLE
            } else{
            recyclerViewList.clear()


            // 장바구니 리스트 데이터를 recyclerViewList에 추가
            recyclerViewList.addAll(productList)

            // 서버에서 이미지 가져오기
            val uris  = async(Dispatchers.IO) {
                // 이미지에 접근할 수 있는 uri를 가져온다.
                ProductService.getImageUris(productList)
            }

            imageUris.postValue(uris.await())

            // checkBoxStates 초기화
            checkBoxStates.value = MutableList(recyclerViewList.size) { false }

            // 수량 리스트 초기화
            productCountList.value = MutableList(recyclerViewList.size) { 1 }

            fragmentShopCartBinding.recyclerViewShopCart.adapter?.notifyDataSetChanged()
            }
        }
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

    // 상품 목록 화면으로 이동하는 메서드
    fun moveHomeFragment(){
        mainFragment.removeFragment(ShopSubFragmentName.SHOP_CART_FRAGMENT)
        mainFragment.removeFragment(ShopSubFragmentName.PRODUCT_INFO_FRAGMENT)
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

    // 전체 선택 시 모든 체크박스를 업데이트하는 메서드
    fun updateAllCheckBoxes(isChecked: Boolean) {
        // 전체 선택 상태에 맞게 모든 체크박스 상태를 갱신
        val updatedStates = MutableList(recyclerViewList.size) { isChecked }

        // checkBoxStates의 상태를 업데이트
        checkBoxStates.value = updatedStates

        // 전체 선택 상태를 반영하기 위해 ViewModel에서 관리하는 checkBoxCartProductAllCheckedState 상태 업데이트
        (fragmentShopCartBinding.shopCartViewModel?.checkBoxCartProductAllCheckedState)?.value = isChecked

        // RecyclerView 어댑터에 데이터 변경을 알리기
        fragmentShopCartBinding.recyclerViewShopCart.adapter?.notifyDataSetChanged()
    }

//    // 상품 수량 선택 메서드
//    fun selectProductCount(isIncrease:Boolean ) {
//
//            val currentCount = rowCartProductListViewModel?.textViewCartProductCntText?.value?.toIntOrNull() ?: 1
//
//            if (isIncrease) {
//                // 수량 증가
//                rowCartProductListViewModel?.textViewCartProductCntText?.value = (currentCount + 1).toString()
//            } else {
//                // 수량 감소 (1 이하로는 감소하지 않음)
//                if (currentCount > 1) {
//                    rowCartProductListViewModel?.textViewCartProductCntText?.value = (currentCount - 1).toString()
//                } else {
//                    shopActivity.runOnUiThread {
//                        Toast.makeText(
//                            shopActivity,
//                            "최소 수량입니다.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            }
//    }


    // 선택 삭제 버튼 메서드
    fun selectionDelete() {
        // 선택된 항목들의 documentId를 저장할 리스트
        var selectedIds = mutableListOf<String>()

        // 체크된 항목들의 ID를 selectedIds에 추가
        checkBoxStates.value?.forEachIndexed { index, isChecked ->
            if (isChecked) {
                val productId = recyclerViewList[index].productDocumentId
                if (productId != null) {
                    selectedIds.add(productId)
                    Log.d("ShopCartFragment", "Added ID: $productId, Selected IDs: $selectedIds")
                }
                //selectedIds.add(recyclerViewList[index].productDocumentId)
                //Log.d("ShopCartFragment", "Selected ID: ${recyclerViewList[index].productDocumentId}")

            }
        }

        if (selectedIds.isNotEmpty()) {
            // 선택된 ID를 가지고 삭제 작업을 수행
            CoroutineScope(Dispatchers.Main).launch {
                val deleteWork = async(Dispatchers.IO) {
                    // 서버에서 삭제
                    selectedIds.forEach { selectedIds ->
                        // 유저의 cartList에서 해당 상품을 삭제
                        UserService.deleteUserCartData(shopActivity.userDocumentID, selectedIds)
                    }
                }
                deleteWork.await()

                // 삭제 후 RecyclerView 업데이트
                loadCartProducts()
            }
        }else{
            // 아무것도 체크 안되어있으면 다이얼로그 띄움
            showNoSelectionDialog()
        }
    }

    // 선택된 항목이 없을 경우 다이얼로그 띄우는 메서드
    fun showNoSelectionDialog() {
        val builder = android.app.AlertDialog.Builder(shopActivity)
        builder.setTitle("선택 삭제하기")
        builder.setMessage("선택된 상목이 없습니다.")
        builder.setPositiveButton("닫기") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
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
                val dataBundle = Bundle()
                dataBundle.putString("productDocumentId", recyclerViewList[cartViewHolder.adapterPosition].productDocumentId)
                mainFragment.replaceFragment(ShopSubFragmentName.PRODUCT_INFO_FRAGMENT, true, true, dataBundle)
            }

            return cartViewHolder
        }

        override fun getItemCount(): Int {
            return recyclerViewList.size
        }

        override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
            val rowViewModel = holder.rowCartProductListBinding.rowCartProductListViewModel!!

            holder.rowCartProductListBinding.rowCartProductListViewModel?.textViewCartProductNameText?.value = recyclerViewList[position].productName
            holder.rowCartProductListBinding.rowCartProductListViewModel?.textViewCartProductPriceText?.value =
                recyclerViewList[position].productPrice.toString()
            holder.rowCartProductListBinding.rowCartProductListViewModel?.textViewCartProductPercentText?.value =
                recyclerViewList[position].productDiscountRate.toString()

            // 이미지 가져오기
            val imageUri = imageUris.value?.get(position)
            if (imageUri != null) {
                shopActivity.showServiceImage(imageUri, holder.rowCartProductListBinding.imageViewCartProduct)
            }

            // productCountList에서 수량을 가져오기
            holder.rowCartProductListBinding.rowCartProductListViewModel?.textViewCartProductCntText?.value = productCountList.value?.get(position)?.toString()

            // 체크박스 상태 바인딩
            val isChecked = checkBoxStates.value?.get(position) ?: false
            rowViewModel.checkBoxCartProductChecked.value = isChecked

            // holder.rowCartProductListBinding.rowCartProductListViewModel?.checkBoxCartProductChecked?.value = isChecked

            holder.rowCartProductListBinding.checkBoxCartProduct.setOnCheckedChangeListener { _, newState ->
                checkBoxStates.value?.let {
                    it[position] = newState
                    checkBoxStates.value = it // LiveData에 새 값을 설정
                }
                fragmentShopCartBinding.recyclerViewShopCart.adapter?.notifyDataSetChanged() // RecyclerView 갱신
            }


            Log.d("ShopCartFragment", "CheckBox states: ${checkBoxStates.value}")



            // 수량 증가 버튼 클릭 시 수량 증가
            holder.rowCartProductListBinding.buttonCartProductCntPlus.setOnClickListener {
                val currentCount = productCountList.value?.get(position) ?: 1
                val newCount = currentCount + 1
                productCountList.value?.set(position, newCount)
                holder.rowCartProductListBinding.rowCartProductListViewModel?.updateProductCount(newCount)
            }

            // 수량 감소 버튼 클릭 시 수량 감소
            holder.rowCartProductListBinding.buttonCartProductCntMinus.setOnClickListener {
                val currentCount = productCountList.value?.get(position) ?: 1
                if (currentCount > 1) {
                    val newCount = currentCount - 1
                    productCountList.value?.set(position, newCount)
                    holder.rowCartProductListBinding.rowCartProductListViewModel?.updateProductCount(newCount)
                } else {
                    Toast.makeText(shopActivity, "최소 수량입니다.", Toast.LENGTH_SHORT).show()
                }
            }
            
        }
    }
}