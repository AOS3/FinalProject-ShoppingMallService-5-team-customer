package com.judamie_user.android.ui.subfragment

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentWriteProductReviewBinding
import com.judamie_user.android.databinding.ItemPhotoBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.util.PictureHandler
import com.judamie_user.android.viewmodel.fragmentviewmodel.WriteProductReviewViewModel
import java.io.File

class WriteProductReviewFragment(val mainFragment: MainFragment) : Fragment() {
    private lateinit var fragmentWriteProductReviewBinding: FragmentWriteProductReviewBinding
    //private val photoList = mutableListOf<String>() // 사진 경로 리스트

    private val photoList = mutableListOf<Uri>() // 사진 파일 리스트

    private lateinit var photoAdapter: PhotoAdapter

    // 사진 가져오기 런처
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var albumLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    // 임시 사진 저장 URI
    private lateinit var tempPhotoUri: Uri
    private lateinit var photoFilePath: String

    // PictureHandler 인스턴스
    private lateinit var pictureHandler: PictureHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentWriteProductReviewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_write_product_review, container, false)
        fragmentWriteProductReviewBinding.writeProductReviewViewModel = WriteProductReviewViewModel(this@WriteProductReviewFragment)
        fragmentWriteProductReviewBinding.lifecycleOwner = viewLifecycleOwner

        // PictureHandler 초기화
        pictureHandler = PictureHandler(requireContext().contentResolver)

        setupRecyclerView()
        initializeLaunchers()

        return fragmentWriteProductReviewBinding.root
    }

    private fun setupRecyclerView() {
        fragmentWriteProductReviewBinding.apply {
            // PhotoAdapter 초기화
            photoAdapter = PhotoAdapter(photoList) { position ->
                if (position in 0 until photoList.size) {
                    photoList.removeAt(position) // Bitmap 리스트에서 삭제
                    Log.d("test",photoList.toString())
                    photoAdapter.notifyItemRemoved(position)
                    photoAdapter.notifyItemRangeChanged(position, photoList.size) // 이후 항목 위치 업데이트
                    updatePhotoCountText()
                } else {
                    showToast("삭제할 항목이 유효하지 않습니다.") // 예외 방지
                }
            }

            // RecyclerView 설정
            recyclerViewWriteProductReviewPhotos.apply {
                layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
                adapter = photoAdapter
            }

            // 초기 사진 개수 텍스트 설정
            updatePhotoCountText()
        }
    }



    private fun initializeLaunchers() {
        // 카메라 런처 초기화
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                processCapturedPhoto(tempPhotoUri)
            } else {
                showToast("사진 촬영 실패")
            }
        }

        // 앨범 런처 초기화
        albumLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { processSelectedPhoto(it) } ?: showToast("사진 선택 취소")
        }
    }

    fun buttonWriteProductReviewAddPhotoOnclick() {
        if (photoList.size < 5) {
            val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
            val bottomSheetDialog = BottomSheetDialog(requireContext())
            bottomSheetDialog.setContentView(bottomSheetView)

            val buttonPhotoPickFromCamera = bottomSheetView.findViewById<TextView>(R.id.buttonPhotoPickFromCamera)
            val buttonPhotoPickFromAlbum = bottomSheetView.findViewById<TextView>(R.id.buttonPhotoPickFromAlbum)

            buttonPhotoPickFromCamera.setOnClickListener {
                val fileName = "temp_${System.currentTimeMillis()}.jpg"
                photoFilePath = requireContext().getExternalFilesDir(null).toString() + "/$fileName"
                val photoFile = File(photoFilePath)
                tempPhotoUri = FileProvider.getUriForFile(
                    requireContext(),
                    "${requireContext().packageName}.provider",
                    photoFile
                )

                cameraLauncher.launch(tempPhotoUri)
                bottomSheetDialog.dismiss()
            }

            buttonPhotoPickFromAlbum.setOnClickListener {
                val request = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                albumLauncher.launch(request)
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.show()
        } else {
            showToast("최대 5장까지 추가할 수 있습니다.")
        }
    }

    private fun processCapturedPhoto(uri: Uri) {
        photoList.add(uri) // Uri를 리스트에 추가
        photoAdapter.notifyItemInserted(photoList.size - 1)
        updatePhotoCountText()
    }

    private fun processSelectedPhoto(uri: Uri) {
        photoList.add(uri) // Uri를 리스트에 추가
        photoAdapter.notifyItemInserted(photoList.size - 1)
        updatePhotoCountText()
    }



    private fun updatePhotoCountText() {
        fragmentWriteProductReviewBinding.textViewTitle.text =
            "사진 첨부 (선택) (${photoList.size}/5)"
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun movePrevFragment() {
        mainFragment.removeFragment(ShopSubFragmentName.WRITE_PRODUCT_REVIEW_FRAGMENT)
    }

    fun saveReview() {
        fragmentWriteProductReviewBinding.apply {
            if (writeProductReviewViewModel?.textInputLayoutWriteProductReviewContentText?.value?.length!! < 10) {
                showToast("리뷰를 최소 10자 이상 작성해주세요")
            } else {
                // 다이얼로그 빌더 생성
                val builder = MaterialAlertDialogBuilder(requireContext())

                val dialog = builder.setTitle("리뷰 저장")
                    .setMessage("작성한 리뷰를 저장하시겠습니까?")
                    .setPositiveButton("확인") { dialog, _ ->
                        // 리뷰 저장 로직 실행
                        showToast("리뷰가 저장되었습니다.")

                        // 현재 프래그먼트 제거
                        mainFragment.removeFragment(ShopSubFragmentName.WRITE_PRODUCT_REVIEW_FRAGMENT)

                        dialog.dismiss()
                    }
                    .setNegativeButton("취소") { dialog, _ ->
                        // 다이얼로그 닫기
                        dialog.dismiss()
                    }
                    .setCancelable(true) // 다이얼로그 외부 터치로 닫을 수 있음
                    .create() // 다이얼로그 생성

                // 배경색을 흰색으로 설정
                dialog.window?.setBackgroundDrawableResource(android.R.color.white)

                dialog.show() // 다이얼로그 표시
            }
        }
    }



    inner class PhotoAdapter(
        private val photos: MutableList<Uri>, // Uri 리스트로 변경
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
            val uri = photos[position]

            // Glide로 Uri를 로드
            Glide.with(holder.binding.imageViewPhoto.context)
                .load(uri)
                .apply(RequestOptions().transform(RoundedCorners(24))) // 둥근 모서리 처리
                .into(holder.binding.imageViewPhoto)

            holder.binding.buttonDeletePhoto.setOnClickListener {
                onDeleteClick(position)
            }
        }

        override fun getItemCount(): Int = photos.size

        inner class PhotoViewHolder(val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root)
    }



}
