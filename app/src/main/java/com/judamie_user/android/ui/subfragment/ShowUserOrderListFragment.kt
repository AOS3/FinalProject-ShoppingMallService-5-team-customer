package com.judamie_user.android.ui.subfragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.judamie_user.android.databinding.FragmentShowUserOrderListBinding
import com.judamie_user.android.databinding.RowOrderListBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.viewmodel.fragmentviewmodel.ShowUserOrderListViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowOrderListViewModel

class ShowUserOrderListFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentShowUserOrderListBinding: FragmentShowUserOrderListBinding

    //  주문목록RecyclerView를 구성하기 위해 사용할 리스트
    var recyclerViewOrderList = Array(50, {
        "항목 ${it + 1}"
    })

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

        //주문 목록 리사이클러뷰를 세팅한다
        settingRecyclerViewWishList()

        //스크롤 위에서 아래로잡아당겨서 업데이트
        refreshData()

        return fragmentShowUserOrderListBinding.root
    }

    //스크롤 위에서 아래로잡아당겨서 업데이트
    private fun refreshData() {
        //찜 목록이 비어있다면 스와이프 비활성화
        if (recyclerViewOrderList.size == 0){
            fragmentShowUserOrderListBinding.swipeRefreshLayout.isEnabled = false
        }
        if (recyclerViewOrderList.size > 0) {
            fragmentShowUserOrderListBinding.apply {
                swipeRefreshLayout.setOnRefreshListener {
                    // 예: 데이터를 다시 로드하거나 UI 업데이트
                    // 업데이트 로직 수행
                    Handler(Looper.getMainLooper()).postDelayed({
                        // 새로 고침 완료 시 로딩 애니메이션 종료
                        fragmentShowUserOrderListBinding.swipeRefreshLayout.isRefreshing = false
                    }, 500) // 0.5초 지연 후 종료
                }
            }
        }
    }

    fun movePrevFragment() {
        mainFragment.removeFragment(ShopSubFragmentName.SHOW_USER_ORDER_LIST_FRAGMENT)
    }

    //주문 목록 리사이클러뷰를 세팅한다
    fun settingRecyclerViewWishList() {
        fragmentShowUserOrderListBinding.apply {
            recyclerViewShowUserOrderList.adapter = RecyclerViewAdapter()
            recyclerViewShowUserOrderList.layoutManager = LinearLayoutManager(requireContext())

            val deco =
                MaterialDividerItemDecoration(requireContext(), MaterialDividerItemDecoration.VERTICAL)

            recyclerViewShowUserOrderList.addItemDecoration(deco)
        }
    }

    inner class RecyclerViewAdapter :
        RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {
        inner class RecyclerViewHolder(val rowOrderListBinding: RowOrderListBinding) :
            RecyclerView.ViewHolder(rowOrderListBinding.root)

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
            return recyclerViewOrderList.size
        }

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            holder.rowOrderListBinding.rowOrderListViewModel?.textViewOrderNameText?.value = recyclerViewOrderList[position]
        }
    }


}
