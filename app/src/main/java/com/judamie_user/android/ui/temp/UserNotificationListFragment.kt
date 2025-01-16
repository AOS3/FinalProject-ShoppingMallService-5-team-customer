package com.judamie_user.android.ui.temp

import android.content.res.ColorStateList
import android.os.Bundle
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
import com.judamie_user.android.databinding.FragmentUserNotificationListBinding
import com.judamie_user.android.databinding.RowNotificationBinding
import com.judamie_user.android.databinding.RowProductListBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.viewmodel.rowviewmodel.RowNotificationViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowProductListViewModel
import com.judamie_user.android.viewmodel.temp.UserNotificationListViewModel


class UserNotificationListFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentUserNotificationListBinding: FragmentUserNotificationListBinding

    //  알림RecyclerView를 구성하기 위해 사용할 리스트
    var recyclerViewUserNotificationList = Array(5, {
        "항목 ${it + 1}"
    })

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

        //리사이클러뷰 세팅
        settingRecyclerView()

        return fragmentUserNotificationListBinding.root
    }

    fun movePrevFragment() {
        mainFragment.removeFragment(ShopSubFragmentName.USER_NOTIFICATION_LIST_FRAGMENT)
    }

    fun settingRecyclerView(){
        fragmentUserNotificationListBinding.apply {
            recyclerViewUserNotificationList.adapter = RecyclerViewAdapter()
            recyclerViewUserNotificationList.layoutManager = LinearLayoutManager(requireContext())
            val deco = MaterialDividerItemDecoration(requireContext(), MaterialDividerItemDecoration.VERTICAL)
            recyclerViewUserNotificationList.addItemDecoration(deco)
        }
    }

    //찜 목록 리사이클러뷰어댑터
    inner class RecyclerViewAdapter :
        RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {

        inner class RecyclerViewHolder(val rowNotificationBinding: RowNotificationBinding) :
            RecyclerView.ViewHolder(rowNotificationBinding.root) {

//            init {
//
//            }
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
            if (recyclerViewUserNotificationList.size == 0) {
                fragmentUserNotificationListBinding.apply {
                    // ViewModel을 통해 visibility 설정
                    userNotificationListViewModel?.setNoNotificationVisibility(true)
                }
            }
            return recyclerViewUserNotificationList.size

//            fragmentUserNotificationListBinding.userNotificationListViewModel?.setNoNotificationVisibility(true)
//            return 0
        }

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            holder.rowNotificationBinding.rowNotificationViewModel?.textViewRowNotificationProductNameText?.value =
                recyclerViewUserNotificationList[position]
        }
    }


}