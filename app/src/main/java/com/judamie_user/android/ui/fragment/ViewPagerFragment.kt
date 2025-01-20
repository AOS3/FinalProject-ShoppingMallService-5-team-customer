package com.judamie_user.android.ui.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.judamie_user.android.R
import com.judamie_user.android.activity.ShopActivity
import com.judamie_user.android.databinding.FragmentViewPagerBinding
import com.judamie_user.android.databinding.RowSearchListBinding
import com.judamie_user.android.viewmodel.fragmentviewmodel.SearchViewModel
import com.judamie_user.android.viewmodel.fragmentviewmodel.ViewPagerViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowSearchListViewModel

class ViewPagerFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentViewPagerBinding: FragmentViewPagerBinding
    lateinit var shopActivity: ShopActivity

    // ReyclerView 구성을 위한 임시 데이터
    val tempList1 = Array(32) {
        "발베니 14년산"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentViewPagerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_pager, container, false)
        fragmentViewPagerBinding.viewPagerViewModel =
            ViewPagerViewModel(this@ViewPagerFragment)
        fragmentViewPagerBinding.lifecycleOwner = this@ViewPagerFragment

        shopActivity = activity as ShopActivity

        // 전달받은 데이터
        val tabIndex = arguments?.getInt("TAB_INDEX") ?: 0
        val tabTitle = arguments?.getString("TAB_TITLE") ?: "Default Title"
        // Log.d("test100", "Index: $tabIndex, Title: $tabTitle")

        // 검색 recyclerView 구성 메서드 호출
        settingSearchRecyclerView()
        // Dropdown ArrayAdapter 연결 메서드 호출
        dropMenuAdapter()

        return fragmentViewPagerBinding.root
    }

    // Tab Index 및 Title에 따라 데이터 표시 메서드
    fun setupTabContent(tabIndex: Int, tabTitle: String) {


    }

    // Dropdown ArrayAdapter 연결 메서드
    fun dropMenuAdapter() {
        val viewArray = resources.getStringArray(R.array.select_view)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item_home, viewArray)
        fragmentViewPagerBinding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    // 홈 RecyclerView 구성 메서드
    fun settingSearchRecyclerView() {
        fragmentViewPagerBinding.apply {
            recyclerViewHome.layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerViewHome.adapter = HomeRecyclerViewAdapter()
            val horizontalDivider = MaterialDividerItemDecoration(
                requireContext(),
                MaterialDividerItemDecoration.HORIZONTAL
            )
            recyclerViewHome.addItemDecoration(horizontalDivider)

            val verticalDivider = MaterialDividerItemDecoration(
                requireContext(),
                MaterialDividerItemDecoration.VERTICAL
            )
            recyclerViewHome.addItemDecoration(verticalDivider)
        }
    }

    // 홈 RecyclerView의 어뎁터
    inner class HomeRecyclerViewAdapter :
        RecyclerView.Adapter<HomeRecyclerViewAdapter.HomeViewHolder>() {
        inner class HomeViewHolder(val rowSearchListBinding: RowSearchListBinding) :
            RecyclerView.ViewHolder(rowSearchListBinding.root) {
            init {
                // 초기 상태 설정
                rowSearchListBinding.imageButtonSearchSetWishList.apply {
                    setImageResource(R.drawable.bookmark_filled_24px) // 초기 아이콘
                    imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(itemView.context, R.color.mainColor)
                    )
                    tag = "filled" // 초기 태그
                }

                // 클릭 이벤트 설정
                rowSearchListBinding.imageButtonSearchSetWishList.setOnClickListener {
                    rowSearchListBinding.apply {
                        val isFilled = imageButtonSearchSetWishList.tag == "filled"

                        if (isFilled) {
                            // Outline으로 변경
                            imageButtonSearchSetWishList.setImageResource(R.drawable.bookmark_outline_24px)
                            imageButtonSearchSetWishList.imageTintList = ColorStateList.valueOf(
                                ContextCompat.getColor(itemView.context, R.color.mainColor)
                            )
                            imageButtonSearchSetWishList.tag = "outline" // 태그 업데이트
                        } else {
                            // Filled로 변경
                            imageButtonSearchSetWishList.setImageResource(R.drawable.bookmark_filled_24px)
                            imageButtonSearchSetWishList.imageTintList = ColorStateList.valueOf(
                                ContextCompat.getColor(itemView.context, R.color.mainColor)
                            )
                            imageButtonSearchSetWishList.tag = "filled" // 태그 업데이트
                        }
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
            val rowSearchListBinding = DataBindingUtil.inflate<RowSearchListBinding>(
                layoutInflater,
                R.layout.row_search_list,
                parent,
                false
            )
            rowSearchListBinding.rowSearchListViewModel =
                RowSearchListViewModel(this@ViewPagerFragment)
            rowSearchListBinding.lifecycleOwner = this@ViewPagerFragment

            // 리사이클러뷰 항목 클릭시 상세 거래 완료 내역 보기 화면으로 이동
            rowSearchListBinding.root.setOnClickListener {
                mainFragment.replaceFragment(ShopSubFragmentName.PRODUCT_INFO_FRAGMENT, true, true, null)
            }

            val homeViewHolder = HomeViewHolder(rowSearchListBinding)

            // 리사이클러뷰 항목 클릭시 상세 거래 완료 내역 보기 화면으로 이동
            rowSearchListBinding.root.setOnClickListener {
                // 사용자가 누른 항목의 게시글 문서 번호를 담아서 전달
                val dataBundle = Bundle()
//                dataBundle.putString("boardDocumentId", recyclerViewList[mainViewHolder.adapterPosition].boardDocumentId)
//
                mainFragment.replaceFragment(ShopSubFragmentName.PRODUCT_INFO_FRAGMENT, true, true, dataBundle)
            }


            return homeViewHolder
        }

        override fun getItemCount(): Int {
            return tempList1.size

            //return 0
        }

        override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
            holder.rowSearchListBinding.rowSearchListViewModel?.textViewSearchProductNameText?.value =
                tempList1[position]

        }
    }

}