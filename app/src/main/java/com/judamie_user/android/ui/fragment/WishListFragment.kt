package com.judamie_user.android.ui.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.judamie_user.android.R
import com.judamie_user.android.activity.ShopActivity
import com.judamie_user.android.databinding.FragmentWishListBinding
import com.judamie_user.android.databinding.RowProductListBinding
import com.judamie_user.android.viewmodel.fragmentviewmodel.WishListViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowProductListViewModel

class WishListFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentWishListBinding: FragmentWishListBinding

    //  찜목록RecyclerView를 구성하기 위해 사용할 리스트
    var recyclerViewWishList = Array(50, {
        "항목 ${it + 1}"
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentWishListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_wish_list, container, false)
        fragmentWishListBinding.wishListViewModel = WishListViewModel(this)
        fragmentWishListBinding.lifecycleOwner = this

        //리사이클러뷰 세팅
        settingRecyclerViewWishList()

        //스크롤 위에서 아래로잡아당겨서 업데이트
        refreshData()

        //툴바설정
        settingMaterialToolbarWishList()



        return fragmentWishListBinding.root
    }

    //툴바설정
    private fun settingMaterialToolbarWishList() {
        fragmentWishListBinding.apply {
            //메뉴의 항목을 눌렀을때
            materialToolbarWishList.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuItemCartInWishListToolbar -> {
                        Log.d("test", "장바구니로 가는 함수")
                        // 장바구니로 가는 함수
                    }
                }
                true
            }
        }
    }

    //스크롤 위에서 아래로잡아당겨서 업데이트
    private fun refreshData() {
        //찜 목록이 비어있다면 스와이프 비활성화
        if (recyclerViewWishList.size == 0) {
            fragmentWishListBinding.swipeRefreshLayout.isEnabled = false
        }
        if (recyclerViewWishList.size > 0) {
            fragmentWishListBinding.apply {
                swipeRefreshLayout.setOnRefreshListener {
                    // 예: 데이터를 다시 로드하거나 UI 업데이트
                    // 업데이트 로직 수행
                    Handler(Looper.getMainLooper()).postDelayed({
                        // 새로 고침 완료 시 로딩 애니메이션 종료
                        fragmentWishListBinding.swipeRefreshLayout.isRefreshing = false
                    }, 500) // 0.5초 지연 후 종료
                }
            }
        }
    }

    //찜 목록 리사이클러뷰를 세팅한다
    fun settingRecyclerViewWishList() {
        fragmentWishListBinding.apply {
            recyclerViewWishList.layoutManager = GridLayoutManager(requireContext(), 2) // 2열
            recyclerViewWishList.adapter = RecyclerViewAdapter()
            val horizontalDivider = MaterialDividerItemDecoration(
                requireContext(),
                MaterialDividerItemDecoration.HORIZONTAL
            )
            recyclerViewWishList.addItemDecoration(horizontalDivider)

            val verticalDivider = MaterialDividerItemDecoration(
                requireContext(),
                MaterialDividerItemDecoration.VERTICAL
            )
            recyclerViewWishList.addItemDecoration(verticalDivider)

        }
    }

    inner class RecyclerViewAdapter :
        RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {

        inner class RecyclerViewHolder(val rowProductListBinding: RowProductListBinding) :
            RecyclerView.ViewHolder(rowProductListBinding.root) {

            init {
                rowProductListBinding.apply {
                    // root 클릭 이벤트 설정
                    root.setOnClickListener {
                        val dataBundle = Bundle()
                        val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION }
                            ?: return@setOnClickListener
                        dataBundle.putString("productDocumentId", recyclerViewWishList[position])
                        mainFragment.replaceFragment(
                            ShopSubFragmentName.PRODUCT_INFO_FRAGMENT,
                            true,
                            true,
                            null
                        )
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
            val rowProductListBinding = DataBindingUtil.inflate<RowProductListBinding>(
                layoutInflater,
                R.layout.row_product_list,
                parent,
                false
            )

            rowProductListBinding.lifecycleOwner = viewLifecycleOwner

            return RecyclerViewHolder(rowProductListBinding)
        }

        override fun getItemCount(): Int {
            if (recyclerViewWishList.isNotEmpty()) {
                fragmentWishListBinding.apply {
                    wishListViewModel?.textViewEmptyWishListText1?.value = ""
                    wishListViewModel?.textViewEmptyWishListText2?.value = ""
                }
            }
            return recyclerViewWishList.size
        }

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            val viewModel = RowProductListViewModel(this@WishListFragment)
            holder.rowProductListBinding.rowProductListViewModel = viewModel

            // LiveData 상태 초기화
            val isWishListed = /* recyclerViewWishList[position].isWishListed */ true // 실제 데이터로 설정
            viewModel.isWishListed.value = isWishListed
        }
    }



}