package com.judamie_user.android.ui.fragment

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.TextView
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

    // 검색 RecyclerView 임시 리스트
    var recyclerViewSearchList = mutableListOf<String>()

    // RecyclerView 구성을 위한 임시 데이터
    val tempList = arrayOf(
        "조니워커 블루",
        "발렌타인",
        "마오타이",
        "조니워커 블랙",
        "발렌타인 12년"
    )

    // 검색어를 담을 변수
    var searchKeyword = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        fragmentSearchBinding.searchViewModel =
            SearchViewModel(this@SearchFragment)
        fragmentSearchBinding.lifecycleOwner = this@SearchFragment

        shopActivity = activity as ShopActivity

        // RecyclerView 초기 visibility 설정
        fragmentSearchBinding.recyclerViewSearch.visibility = View.GONE

        // RecyclerView 구성을 위한 리스트를 초기화한다.
        recyclerViewSearchList.clear()

        // 키보드 활성화 및 포커스 처리
        fragmentSearchBinding.editTextSearchInput.post {
            shopActivity.showSoftInput(fragmentSearchBinding.editTextSearchInput)
        }

        // 툴바 구성 메서드 호출
        settingToolbar()
        // 검색 설정 메서드 호출
        settingSearchView()
        // 검색 결과 갱신 메서드 호출
        refreshSearchRecyclerView()
        // Search RecyclerView 설정 메서드 호출
        settingSearchRecyclerView()

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

    fun settingSearchView() {
        fragmentSearchBinding.apply {
            editTextSearchInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // 실시간 검색어 갱신
                    searchKeyword = s?.toString()?.trim() ?: ""

                    // 로그로 검색어 출력
                    Log.d("test100", "실시간 입력된 검색어: $searchKeyword")

                    // 검색 결과 갱신
                    refreshSearchRecyclerView()
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }


    // 검색 RecyclerView 구성 메서드
    fun settingSearchRecyclerView() {
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
                R.layout.row_search_list,
                parent,
                false
            )
            rowSearchListBinding.rowSearchListViewModel =
                RowSearchListViewModel(this@SearchFragment)
            rowSearchListBinding.lifecycleOwner = this@SearchFragment

            // 리사이클러뷰 항목 클릭시 상세 거래 완료 내역 보기 화면으로 이동
            rowSearchListBinding.root.setOnClickListener {
                mainFragment.replaceFragment(ShopSubFragmentName.PRODUCT_INFO_FRAGMENT, true, true, null)
            }

            val searchViewHolder = SearchViewHolder(rowSearchListBinding)

            // 리사이클러뷰 항목 클릭시 상세 거래 완료 내역 보기 화면으로 이동
            rowSearchListBinding.root.setOnClickListener {
                // 사용자가 누른 항목의 게시글 문서 번호를 담아서 전달
                val dataBundle = Bundle()
//                dataBundle.putString("boardDocumentId", recyclerViewList[mainViewHolder.adapterPosition].boardDocumentId)
//
                mainFragment.replaceFragment(ShopSubFragmentName.PRODUCT_INFO_FRAGMENT, true, true, dataBundle)
            }


            return searchViewHolder
        }

        override fun getItemCount(): Int {
            if (recyclerViewSearchList.size > 0) {
                fragmentSearchBinding.apply {
                    textViewSearchEmptyProduct?.text = "일치하는 상품이 없습니다.\uD83D\uDE22"
                }
            }
            return recyclerViewSearchList.size

            //return 0
        }

        override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
            holder.rowSearchListBinding.rowSearchListViewModel?.textViewSearchProductNameText?.value =
                recyclerViewSearchList[position]

        }
    }

    // 검색 결과를 가져와 RecyclerView를 갱신하는 메서드
    fun refreshSearchRecyclerView() {
        val keyword = fragmentSearchBinding.editTextSearchInput.toString()

        recyclerViewSearchList.clear()
        recyclerViewSearchList.addAll(tempList.filter { it.contains(searchKeyword, ignoreCase = true) })
        // 검색에 결과가 없으면
        if (recyclerViewSearchList.isEmpty()) {
            // 메시지를 표시
            fragmentSearchBinding.textViewSearchEmptyProduct.visibility = View.VISIBLE
            fragmentSearchBinding.recyclerViewSearch.visibility = View.GONE
        } else {
            // 결과가 있으면 리사이클러뷰 표시
            fragmentSearchBinding.textViewSearchEmptyProduct.visibility = View.GONE
            fragmentSearchBinding.recyclerViewSearch.visibility = View.VISIBLE
        }
        // 검색 결과에 맞는 RecyclerView의 어댑터를 갱신
        fragmentSearchBinding.recyclerViewSearch.adapter?.notifyDataSetChanged()
    }

}
