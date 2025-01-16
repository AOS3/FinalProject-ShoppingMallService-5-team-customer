package com.judamie_user.android.ui.fragment

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.judamie_user.android.R
import com.judamie_user.android.activity.LoginActivity
import com.judamie_user.android.activity.ShopActivity
import com.judamie_user.android.databinding.FragmentSearchBinding
import com.judamie_user.android.databinding.RowCartProductListBinding
import com.judamie_user.android.databinding.RowSearchListBinding
import com.judamie_user.android.viewmodel.fragmentviewmodel.RegisterVerificationViewModel
import com.judamie_user.android.viewmodel.fragmentviewmodel.SearchViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowCartProductListViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowSearchListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class SearchFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentSearchBinding: FragmentSearchBinding
    lateinit var shopActivity: ShopActivity

    //  RecyclerView를 구성하기 위해 사용할 리스트
    var searchList = Array(50, {
        "항목 ${it + 1}"
    })
    // 검색어를 담을 변수
    var searchKeyword = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSearchBinding.searchViewModel =
            SearchViewModel(this@SearchFragment)
        fragmentSearchBinding.lifecycleOwner = this@SearchFragment

        shopActivity = activity as ShopActivity

        // textViewSearchEmptyProduct - visibility : gone

        // 툴바 구성 메서드 호출
        settingToolbar()
        // Search RecyclerView 설정 메서드 호출
        settingCartRecyclerView()

        return fragmentSearchBinding.root
    }

    // 툴바 구성 메서드
    fun settingToolbar() {
        fragmentSearchBinding.searchViewModel?.apply {
            // 타이틀
            toolbarSearchTitle.value = "검색하기"
            // 네비게이션 아이콘
            toolbarSearchOnNavigationIcon.value = R.drawable.arrow_back_ios_24px
        }
    }

    // 이전 화면으로 돌아가는 메서드
    fun movePrevFragment() {
        mainFragment.removeFragment(ShopSubFragmentName.SEARCH_FRAGMENT)
    }

    // 검색 RecyclerView 구성 메서드
    fun settingCartRecyclerView() {
        fragmentSearchBinding.apply {
            recyclerViewSearch.layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerViewSearch.adapter = SearchRecyclerViewAdapter()
            val horizontalDivider = MaterialDividerItemDecoration(
                requireContext(),
                MaterialDividerItemDecoration.HORIZONTAL
            )
            recyclerViewSearch.addItemDecoration(horizontalDivider)

            val verticalDivider = MaterialDividerItemDecoration(
                requireContext(),
                MaterialDividerItemDecoration.VERTICAL
            )
            recyclerViewSearch.addItemDecoration(verticalDivider)
        }
    }

    // 검색 RecyclerView의 어뎁터
    inner class SearchRecyclerViewAdapter :
        RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder>() {
        inner class SearchViewHolder(val rowSearchListBinding: RowSearchListBinding) :
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

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
            val rowSearchListBinding = DataBindingUtil.inflate<RowSearchListBinding>(
                layoutInflater,
                R.layout.row_cart_product_list,
                parent,
                false
            )
            rowSearchListBinding.rowSearchListViewModel =
                RowSearchListViewModel(this@SearchFragment)
            rowSearchListBinding.lifecycleOwner = this@SearchFragment


            val searchViewHolder = SearchViewHolder(rowSearchListBinding)

            rowSearchListBinding.root.setOnClickListener {
                // 사용자가 누른 항목의 게시글 문서 번호를 담아서 전달
//                val dataBundle = Bundle()
//                dataBundle.putString("boardDocumentId", recyclerViewList[mainViewHolder.adapterPosition].boardDocumentId)
//                boardMainFragment.replaceFragment(BoardSubFragmentName.BOARD_READ_FRAGMENT, true, true, dataBundle)
            }

            return searchViewHolder
        }

        override fun getItemCount(): Int {
            if (searchList.size > 0) {
                fragmentSearchBinding.apply {
                    textViewSearchEmptyProduct?.text = "일치하는 상품이 없습니다.\uD83D\uDE22"
                }
            }
            return searchList.size

            //return 0
        }

        override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
            holder.rowSearchListBinding.rowSearchListViewModel?.textViewSearchProductNameText?.value =
                searchList[position]

        }
    }

    // 검색 처리
    fun proSearch() {
        fragmentSearchBinding.apply {
            // 검색창에 포커스를 둔다.
            shopActivity.showSoftInput(editTextSearchInput!!)
            // 키보드의 엔터를 누르면 동작하는 리스너
//            editTextSearchInput.setOnClickListener { v, actionId, event ->
//                // 검색 데이터를 가져와 보여준다.
//                CoroutineScope(Dispatchers.Main).launch {
//                    val work1 = async(Dispatchers.IO) {
//                        val keyword = editTextSearchInput?.text.toString()
//                        ShopRepository.selectProductDataAllByProductName(shopActivity, keyword)
//                    }
//                    searchList = work1.awat()
//                    recyclerViewSearch.adapter?.notifyDataSetChanged()
//                }
//                shopActivity.hideSoftInput()
//                true
//            }
        }
    }

    // 실시간으로 검색
    fun searchWheneverItChanges() {

    }

    // 검색 결과를 가져와 RecyclerView를 갱신하는 메서드
    fun refreshSearchRecyclerView(){

    }
}
