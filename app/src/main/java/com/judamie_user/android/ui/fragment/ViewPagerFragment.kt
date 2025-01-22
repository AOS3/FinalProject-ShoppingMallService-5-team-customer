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
import com.judamie_user.android.firebase.model.ProductModel
import com.judamie_user.android.firebase.service.ProductService
import com.judamie_user.android.util.ProductCategory
import com.judamie_user.android.util.tools.Companion.formatToComma
import com.judamie_user.android.viewmodel.fragmentviewmodel.ViewPagerViewModel
import com.judamie_user.android.viewmodel.rowviewmodel.RowSearchListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ViewPagerFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentViewPagerBinding: FragmentViewPagerBinding
    lateinit var shopActivity: ShopActivity

//    // ReyclerView 구성을 위한 임시 데이터
//    val tempList1 = Array(32) {
//        "발베니 14년산"
//    }

    // 제품 카테고리 값을 담을 변수
    var categoryName:String? = null
    lateinit var productCategory: ProductCategory

    // 메인 RecyclerView를 구성하기 위해 사용할 리스트
    var recyclerViewCategoryList = mutableListOf<ProductModel>()

    companion object {
        private const val ARG_CATEGORY_NAME = "categoryName"

        fun newInstance(categoryName: String, mainFragment: MainFragment): ViewPagerFragment {
            val fragment = ViewPagerFragment(mainFragment)
            val args = Bundle()
            args.putString(ARG_CATEGORY_NAME, categoryName)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentViewPagerBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_view_pager, container, false)
        fragmentViewPagerBinding.viewPagerViewModel =
            ViewPagerViewModel(this@ViewPagerFragment)
        fragmentViewPagerBinding.lifecycleOwner = this@ViewPagerFragment

        shopActivity = activity as ShopActivity

        // 전달받은 categoryName 처리
        categoryName = arguments?.getString(ARG_CATEGORY_NAME)
        // Log.d("test100", "Selected categoryName: $categoryName")

        // categoryName으로 ProductCategory 설정
        productCategory = ProductCategory.values().find { it.str == categoryName }
            ?: ProductCategory.PRODUCT_CATEGORY_DEFAULT
        Log.d("test100", "ProductCategory: ${productCategory.str}")

        // RecyclerView 구성을 위한 리스트를 초기화
        recyclerViewCategoryList.clear()

        categoryName = arguments?.getString(ARG_CATEGORY_NAME)



        // 검색 recyclerView 구성 메서드 호출
        settingSearchRecyclerView()
        // Dropdown ArrayAdapter 연결 메서드 호출
        dropMenuAdapter()
        // 데이터를 가져와 RecyclerView 갱신 메서드 호출
        refreshMainRecyclerView()

        return fragmentViewPagerBinding.root
    }

    // Dropdown ArrayAdapter 연결 메서드
    fun dropMenuAdapter() {
        val viewArray = resources.getStringArray(R.array.select_view)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item_home, viewArray)
        fragmentViewPagerBinding.autoCompleteTextView.setAdapter(arrayAdapter)
    }


    // RecyclerView를 갱신하는 메서드
    fun refreshMainRecyclerView(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                ProductService.gettingProductList(productCategory)
            }
            recyclerViewCategoryList.clear()
            recyclerViewCategoryList.addAll(work1.await())
            fragmentViewPagerBinding.recyclerViewHome.adapter?.notifyDataSetChanged()
        }
    }

    // 홈 RecyclerView 구성 메서드
    fun settingSearchRecyclerView() {
        fragmentViewPagerBinding.apply {
            recyclerViewHome.layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerViewHome.adapter = HomeRecyclerViewAdapter()
//            val horizontalDivider = MaterialDividerItemDecoration(
//                requireContext(),
//                MaterialDividerItemDecoration.HORIZONTAL
//            )
//            recyclerViewHome.addItemDecoration(horizontalDivider)
//
//            val verticalDivider = MaterialDividerItemDecoration(
//                requireContext(),
//                MaterialDividerItemDecoration.VERTICAL
//            )
//            recyclerViewHome.addItemDecoration(verticalDivider)
        }
    }

    // 홈 RecyclerView 어뎁터
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

            val homeViewHolder = HomeViewHolder(rowSearchListBinding)

            // 사용자가 누른 항목의 제품 DocumentId를 담아 전달
            rowSearchListBinding.root.setOnClickListener {
                val dataBundle = Bundle()
                dataBundle.putString(
                    "productDocumentId",
                    recyclerViewCategoryList[homeViewHolder.adapterPosition].productDocumentId
                )
                mainFragment.replaceFragment(
                    ShopSubFragmentName.PRODUCT_INFO_FRAGMENT,
                    true,
                    true,
                    dataBundle
                )
            }

            return homeViewHolder
        }

        override fun getItemCount(): Int {
            return recyclerViewCategoryList.size

            //return 0
        }

        override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
            holder.rowSearchListBinding.rowSearchListViewModel?.imageViewSearchProductSrcCompact?.value = recyclerViewCategoryList[position].productMainImage
            holder.rowSearchListBinding.rowSearchListViewModel?.textViewSearchProductNameText?.value =
                recyclerViewCategoryList[position].productName
            holder.rowSearchListBinding.rowSearchListViewModel?.textViewSearchDiscountRatingText?.value =
                "${recyclerViewCategoryList[position].productDiscountRate}%"
            holder.rowSearchListBinding.rowSearchListViewModel?.textViewSearchProductReviewText?.value =
                "${recyclerViewCategoryList[position].productPrice.formatToComma()}"
            // TODO
            // ProductData에 데이터 추가 예정
//            holder.rowSearchListBinding.rowSearchListViewModel?.textViewSearchProductReviewText?.value =
//                "${recyclerViewCategoryList[position].ProductReview}"
//            holder.rowSearchListBinding.rowSearchListViewModel?.textViewSearchProductSellerText?.value =
//                "${recyclerViewCategoryList[position].productSeller}%"


        }
    }
}