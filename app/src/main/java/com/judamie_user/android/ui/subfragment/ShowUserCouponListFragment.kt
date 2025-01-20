package com.judamie_user.android.ui.subfragment

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
import com.judamie_user.android.databinding.FragmentShowUserCouponListBinding
import com.judamie_user.android.databinding.RowCouponBinding
import com.judamie_user.android.databinding.RowOrderListBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.viewmodel.fragmentviewmodel.ShowUserCouponListViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowCouponViewModel

class ShowUserCouponListFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentShowUserCouponListBinding: FragmentShowUserCouponListBinding

    //  쿠폰RecyclerView를 구성하기 위해 사용할 리스트
    var recyclerViewCouponList = Array(5, {
        "항목 ${it + 1}"
    })


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShowUserCouponListBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_show_user_coupon_list,container,false)
        fragmentShowUserCouponListBinding.showUserCouponListViewModel = ShowUserCouponListViewModel(this)
        fragmentShowUserCouponListBinding.lifecycleOwner = viewLifecycleOwner

        settingRecyclerViewWishList()

        return fragmentShowUserCouponListBinding.root
    }

    fun movePrevFragment() {
        mainFragment.removeFragment(ShopSubFragmentName.SHOW_USER_COUPON_LIST_FRAGMENT)
    }

    // 리사이클러뷰세팅
    fun settingRecyclerViewWishList() {
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
            return recyclerViewCouponList.size
        }

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            holder.rowCouponBinding.rowCouponViewModel?.textViewRowCouponNameText?.value = recyclerViewCouponList[position]
        }
    }

}