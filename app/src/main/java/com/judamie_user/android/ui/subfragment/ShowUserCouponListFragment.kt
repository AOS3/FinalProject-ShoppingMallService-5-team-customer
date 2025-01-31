package com.judamie_user.android.ui.subfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.judamie_manager.firebase.model.CouponModel
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentShowUserCouponListBinding
import com.judamie_user.android.databinding.RowCouponBinding
import com.judamie_user.android.databinding.RowOrderListBinding
import com.judamie_user.android.firebase.service.CouponService
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.util.tools.Companion.toFormattedDate
import com.judamie_user.android.viewmodel.fragmentviewmodel.ShowUserCouponListViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowCouponViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ShowUserCouponListFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentShowUserCouponListBinding: FragmentShowUserCouponListBinding

    //  쿠폰RecyclerView를 구성하기 위해 사용할 리스트
    var recyclerViewCouponList = Array(5, {
        "항목 ${it + 1}"
    })

    var couponList = mutableListOf<CouponModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShowUserCouponListBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_show_user_coupon_list,container,false)
        fragmentShowUserCouponListBinding.showUserCouponListViewModel = ShowUserCouponListViewModel(this)
        fragmentShowUserCouponListBinding.lifecycleOwner = viewLifecycleOwner

        settingRecyclerViewCouponList()

        refreshRecyclerViewCouponList()

        return fragmentShowUserCouponListBinding.root
    }

    fun movePrevFragment() {
        mainFragment.removeFragment(ShopSubFragmentName.SHOW_USER_COUPON_LIST_FRAGMENT)
    }

    //쿠폰리스트를 가져와
    fun refreshRecyclerViewCouponList(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                CouponService.gettingAllCoupons() // 모든 쿠폰 데이터를 가져오기
            }
            val allCoupons = work1.await()
            couponList.addAll(allCoupons)
            fragmentShowUserCouponListBinding.recyclerViewShowUserCouponList.adapter?.notifyDataSetChanged()


        }
    }

    // 리사이클러뷰세팅
    fun settingRecyclerViewCouponList() {
        fragmentShowUserCouponListBinding.apply {
            recyclerViewShowUserCouponList.adapter = RecyclerViewAdapter()
            recyclerViewShowUserCouponList.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    inner class RecyclerViewAdapter :
        RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {
        inner class RecyclerViewHolder(val rowCouponBinding: RowCouponBinding) :
            RecyclerView.ViewHolder(rowCouponBinding.root){
//            init {
//                rowCouponBinding.root.setOnClickListener {
//                    mainFragment.replaceFragment(ShopSubFragmentName.SHOW_USER_ORDER_INFO_FRAGMENT,true,true,null)
//                }
//            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
            val rowCouponBinding = DataBindingUtil.inflate<RowCouponBinding>(
                layoutInflater,
                R.layout.row_coupon,
                parent,
                false
            )

            rowCouponBinding.rowCouponViewModel = RowCouponViewModel(this@ShowUserCouponListFragment)
            rowCouponBinding.lifecycleOwner = viewLifecycleOwner

            return RecyclerViewHolder(rowCouponBinding)
        }

        override fun getItemCount(): Int {
            return couponList.size
        }

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            holder.rowCouponBinding.rowCouponViewModel?.textViewRowCouponNameText?.value = couponList[position].couponName
            holder.rowCouponBinding.rowCouponViewModel?.textViewRowCouponExpireDateText?.value = "${couponList[position].couponPeriod.toFormattedDate()}"
            holder.rowCouponBinding.rowCouponViewModel?.textViewRowCouponDiscountRateText?.value = "${couponList[position].couponDiscountRate}%"
            holder.rowCouponBinding.rowCouponViewModel?.textViewRowCouponStateText?.value = couponList[position].couponState.str
        }
    }

}