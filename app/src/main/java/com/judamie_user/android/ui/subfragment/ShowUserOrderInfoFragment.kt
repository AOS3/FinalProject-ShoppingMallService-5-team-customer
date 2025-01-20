package com.judamie_user.android.ui.subfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentShowUserOrderInfoBinding
import com.judamie_user.android.databinding.RowOrderInfoBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.viewmodel.fragmentviewmodel.ShowUserOrderInfoViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowOrderInfoViewModel

class ShowUserOrderInfoFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentShowUserOrderInfoBinding: FragmentShowUserOrderInfoBinding

    // 주문 상세 리스트를 담을 임시데이터
    var recyclerViewShowUserOrderInfoList = Array(5, {
        "항목 ${it + 1}"
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShowUserOrderInfoBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_show_user_order_info,container,false)
        fragmentShowUserOrderInfoBinding.showUserOrderInfoViewModel = ShowUserOrderInfoViewModel(this)
        fragmentShowUserOrderInfoBinding.lifecycleOwner = viewLifecycleOwner

        //리사이클러뷰세팅
        settingRecyclerView()

        return fragmentShowUserOrderInfoBinding.root
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
                    mainFragment.replaceFragment(ShopSubFragmentName.WRITE_PRODUCT_REVIEW_FRAGMENT,true,true,null)
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
            return recyclerViewShowUserOrderInfoList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.rowOrderInfoBinding.rowOrderInfoViewModel?.textViewRowOrderInfoProductNameText?.value = recyclerViewShowUserOrderInfoList[position]
        }
    }


}