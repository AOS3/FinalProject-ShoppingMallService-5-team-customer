package com.judamie_user.android.ui.subfragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentReviewImageBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.viewmodel.fragmentviewmodel.ReviewImageViewModel

class ReviewImageFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentReviewImageBinding: FragmentReviewImageBinding

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentReviewImageBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_review_image,container,false)
        fragmentReviewImageBinding.viewModel = ReviewImageViewModel(this)
        fragmentReviewImageBinding.lifecycleOwner = viewLifecycleOwner

        gettingImageUri()

        return fragmentReviewImageBinding.root
    }

    fun gettingImageUri(){
        fragmentReviewImageBinding.apply {
            // arguments로 전달된 Bundle에서 "image_uri" 키로 Uri 데이터 가져오기
            imageUri = arguments?.getParcelable("image_uri")

            Glide.with(imageViewReviewImage)
                .load(imageUri) // Firebase Storage URL
                .placeholder(R.drawable.img) // 로드 중 기본 이미지
                .error(R.drawable.img) // 로드 실패 시 기본 이미지
                .into(imageViewReviewImage) // 대상 ImageView

        }
    }

    fun movePrevFragment() {
        mainFragment.removeFragment(ShopSubFragmentName.REVIEW_IMAGE_FRAGMENT)
    }

}