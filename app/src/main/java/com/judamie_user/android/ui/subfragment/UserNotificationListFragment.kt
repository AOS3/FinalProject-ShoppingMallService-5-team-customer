package com.judamie_user.android.ui.subfragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.judamie_user.android.R
import com.judamie_user.android.activity.ShopActivity
import com.judamie_user.android.databinding.FragmentUserNotificationListBinding
import com.judamie_user.android.databinding.RowNotificationBinding
import com.judamie_user.android.databinding.RowProductListBinding
import com.judamie_user.android.firebase.model.OrderModel
import com.judamie_user.android.firebase.service.OrderService
import com.judamie_user.android.firebase.service.ProductService
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.util.OrderState
import com.judamie_user.android.viewmodel.fragmentviewmodel.UserNotificationListViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowNotificationViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowProductListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserNotificationListFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentUserNotificationListBinding: FragmentUserNotificationListBinding
    lateinit var shopActivity: ShopActivity

    var orderList = mutableListOf<OrderModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentUserNotificationListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_user_notification_list,
            container,
            false
        )
        val viewModel = UserNotificationListViewModel(this)
        fragmentUserNotificationListBinding.userNotificationListViewModel = viewModel
        fragmentUserNotificationListBinding.lifecycleOwner = viewLifecycleOwner
        shopActivity = activity as ShopActivity

        // 유저 아이디를 통해 오더데이터 가져오기
        gettingOrdersByUserID()

        return fragmentUserNotificationListBinding.root
    }

    // 유저 아이디를 통해 오더데이터 가져오기
    fun gettingOrdersByUserID() {
        fragmentUserNotificationListBinding.apply {
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO) {
                    OrderService.gettingOrdersByUserID(shopActivity.userDocumentID)
                }
                orderList = work1.await()

                orderList.forEach {
                    Log.d("orderList", it.orderDocumentId)
                }

                // 리사이클러뷰 세팅 (suspend 함수로 변경)
                settingRecyclerView()

                // 리사이클러뷰 세팅 완료 후 ProgressBar 숨기고 RecyclerView 표시
                userNotificationListViewModel?.setProgressBarUserNotificationVisibleVisibility(false)
                userNotificationListViewModel?.setRecyclerViewUserNotificationListVisibleVisibility(true)
            }
        }
    }


    fun movePrevFragment() {
        mainFragment.removeFragment(ShopSubFragmentName.USER_NOTIFICATION_LIST_FRAGMENT)
    }

    suspend fun settingRecyclerView() {
        withContext(Dispatchers.Main) {
            fragmentUserNotificationListBinding.apply {
                recyclerViewUserNotificationList.adapter = RecyclerViewAdapter()
                recyclerViewUserNotificationList.layoutManager =
                    LinearLayoutManager(requireContext())
                val deco = MaterialDividerItemDecoration(
                    requireContext(),
                    MaterialDividerItemDecoration.VERTICAL
                )
                recyclerViewUserNotificationList.addItemDecoration(deco)
            }
        }
    }

    //찜 목록 리사이클러뷰어댑터
    inner class RecyclerViewAdapter :
        RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {

        private var boundItemsCount = 0 // 바인딩된 아이템 수 카운트

        inner class RecyclerViewHolder(val rowNotificationBinding: RowNotificationBinding) :
            RecyclerView.ViewHolder(rowNotificationBinding.root) {
            init {
                rowNotificationBinding.root.setOnClickListener {
                    mainFragment.replaceFragment(
                        ShopSubFragmentName.SHOW_USER_ORDER_LIST_FRAGMENT,
                        true,
                        true,
                        null
                    )
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
            val rowNotificationBinding = DataBindingUtil.inflate<RowNotificationBinding>(
                layoutInflater,
                R.layout.row_notification,
                parent,
                false
            )
            rowNotificationBinding.rowNotificationViewModel =
                RowNotificationViewModel(this@UserNotificationListFragment)
            rowNotificationBinding.lifecycleOwner = viewLifecycleOwner

            return RecyclerViewHolder(rowNotificationBinding)
        }

        override fun getItemCount(): Int {
            val count = orderList.count { it.orderState > OrderState.ORDER_STATE_DELIVERY }
            if (count == 0) {
                fragmentUserNotificationListBinding.userNotificationListViewModel?.setNoNotificationVisibility(true)
            }
            return count
        }

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            if (orderList[position].orderState > OrderState.ORDER_STATE_DELIVERY) {
                holder.rowNotificationBinding.rowNotificationViewModel?.apply {
                    CoroutineScope(Dispatchers.Main).launch {
                        val work1 = async(Dispatchers.IO) {
                            ProductService.gettingProductOne(orderList[position].productDocumentId)
                        }
                        val productModel = work1.await()

                        textViewRowNotificationProductNameText?.value = productModel.productName

                        val work2 = async(Dispatchers.IO) {
                            ProductService.gettingImage(productModel.productMainImage)
                        }
                        val uri = work2.await()

                        imageViewRowNotificationProductImageUri?.value = uri
                    }
                }
            }

        }
    }


}