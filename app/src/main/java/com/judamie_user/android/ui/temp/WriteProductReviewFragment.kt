package com.judamie_user.android.ui.temp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentWriteProductReviewBinding
import com.judamie_user.android.databinding.ItemPhotoBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.viewmodel.temp.WriteProductReviewViewModel

class WriteProductReviewFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentWriteProductReviewBinding: FragmentWriteProductReviewBinding
    private val photoList = mutableListOf<String>() // 사진 경로 리스트
    private lateinit var photoAdapter: PhotoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentWriteProductReviewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_write_product_review, container, false)
        fragmentWriteProductReviewBinding.writeProductReviewViewModel = WriteProductReviewViewModel(this)
        fragmentWriteProductReviewBinding.lifecycleOwner = viewLifecycleOwner

        setupRecyclerView()

        return fragmentWriteProductReviewBinding.root
    }

    private fun setupRecyclerView() {
        fragmentWriteProductReviewBinding.apply {
            photoAdapter = PhotoAdapter(photoList) { position ->
                // 사진 삭제 처리
                photoList.removeAt(position)
                photoAdapter.notifyItemRemoved(position)
                photoAdapter.notifyItemRangeChanged(position, photoList.size - position)
                updatePhotoCountText() // 사진 개수 텍스트 갱신
            }


            recyclerViewWriteProductReviewPhotos.apply {
                layoutManager =
                    GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
                adapter = photoAdapter
            }


            // 초기 사진 개수 텍스트 설정
            updatePhotoCountText()
        }

    }

    //사진을 추가하는버튼을 구성
    fun buttonWriteProductReviewAddPhotoOnclick() {

        if (photoList.size < 5) {
            photoList.add("photo_path_placeholder") // 임시 사진 경로 추가
            photoAdapter.notifyItemInserted(photoList.size - 1)
            updatePhotoCountText() // 사진 개수 텍스트 갱신
        } else {
            showToast("최대 5장까지 추가할 수 있습니다.")
        }

    }


    // 사진 개수 텍스트 갱신 메서드
    private fun updatePhotoCountText() {
        // val photoCountText = "사진 첨부 (선택) (${photoList.size}/5)"
        fragmentWriteProductReviewBinding.writeProductReviewViewModel?.buttonWriteProductReviewAddPhotoText?.value =
            "사진 추가 \n (${photoList.size}/5)"

        //fragmentWriteProductReviewBinding.textViewTitle.text = photoCountText
    }

    // 토스트 메시지 표시 메서드
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun movePrevFragment() {
        mainFragment.removeFragment(ShopSubFragmentName.WRITE_PRODUCT_REVIEW_FRAGMENT)
    }


    // RecyclerView 어댑터
    inner class PhotoAdapter(
        private val photos: MutableList<String>,
        private val onDeleteClick: (Int) -> Unit
    ) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
            val binding = DataBindingUtil.inflate<ItemPhotoBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_photo,
                parent,
                false
            )
            return PhotoViewHolder(binding)
        }

        override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
            val photoPath = photos[position]
            Glide.with(holder.itemView.context)
                .load(photoPath)
                .centerCrop()
                .into(holder.binding.imageViewPhoto)

            holder.binding.buttonDeletePhoto.setOnClickListener {
                onDeleteClick(position) // 삭제 버튼 클릭 시 콜백 호출
            }
        }


        override fun getItemCount(): Int = photos.size

        inner class PhotoViewHolder(val binding: ItemPhotoBinding) :
            RecyclerView.ViewHolder(binding.root)
    }
}
