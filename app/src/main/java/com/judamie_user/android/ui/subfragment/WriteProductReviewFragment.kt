package com.judamie_user.android.ui.subfragment

import android.graphics.Bitmap
import android.media.ExifInterface
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
import com.judamie_user.android.firebase.model.ReviewModel
import com.judamie_user.android.firebase.service.ReviewService
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.util.PictureHandler
import com.judamie_user.android.viewmodel.fragmentviewmodel.WriteProductReviewViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WriteProductReviewFragment(val mainFragment: MainFragment) : Fragment() {
    private lateinit var fragmentWriteProductReviewBinding: FragmentWriteProductReviewBinding
    //private val photoList = mutableListOf<String>() // 사진 경로 리스트

    private val photoList = mutableListOf<Uri>() // 사진 파일 리스트

    private val photoFileNameList = mutableListOf<String>() // 저장되는 이름의 사진 파일이름

    private lateinit var photoAdapter: PhotoAdapter

    // 사진 가져오기 런처
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var albumLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    // 촬영된 사진이 위치할 경로
    lateinit var filePath: String

    // 임시 사진 저장 URI
    private lateinit var tempPhotoUri: Uri
    private lateinit var photoFilePath: String

    // PictureHandler 인스턴스
    private lateinit var pictureHandler: PictureHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentWriteProductReviewBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_write_product_review,
            container,
            false
        )
        fragmentWriteProductReviewBinding.writeProductReviewViewModel =
            WriteProductReviewViewModel(this@WriteProductReviewFragment)
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
                    photoList.removeAt(position)
                    if (position < photoFileNameList.size) {
                        photoFileNameList.removeAt(position)
                    }
                    Log.d("test", "Updated photoList: $photoList")
                    Log.d("test", "Updated photoFileNameList: $photoFileNameList")


                    photoAdapter.notifyItemRemoved(position)
                    photoAdapter.notifyItemRangeChanged(position, photoList.size) // 이후 항목 위치 업데이트
                    updatePhotoCountText()
                } else {
                    showToast("삭제할 항목이 유효하지 않습니다.") // 예외 방지
                }
            }

            // RecyclerView 설정
            recyclerViewWriteProductReviewPhotos.apply {
                layoutManager =
                    GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
                adapter = photoAdapter
            }

            // 초기 사진 개수 텍스트 설정
            updatePhotoCountText()
        }
    }


    private fun initializeLaunchers() {
        // 카메라 런처 초기화
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    processCapturedPhoto(tempPhotoUri)
                } else {
                    showToast("사진 촬영 실패")
                }
            }

        // 앨범 런처 초기화
        albumLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                uri?.let { processSelectedPhoto(it) } ?: showToast("사진 선택 취소")
            }
    }

    fun buttonWriteProductReviewAddPhotoOnclick() {
        if (photoList.size < 5) {
            val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
            val bottomSheetDialog = BottomSheetDialog(requireContext())
            bottomSheetDialog.setContentView(bottomSheetView)

            val buttonPhotoPickFromCamera =
                bottomSheetView.findViewById<TextView>(R.id.buttonPhotoPickFromCamera)
            val buttonPhotoPickFromAlbum =
                bottomSheetView.findViewById<TextView>(R.id.buttonPhotoPickFromAlbum)

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
                val request =
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
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
            val reviewRating = writeProductReviewViewModel?.ratingBarWriteProductReviewRate?.value
            val reviewContent = writeProductReviewViewModel?.textInputLayoutWriteProductReviewContentText?.value
            val reviewTimestamp = System.currentTimeMillis()
            val reviewDate = getCurrentDate()

            if (reviewContent.isNullOrEmpty() || reviewContent.length < 10) {
                showToast("리뷰를 최소 10자 이상 작성해주세요")
                return
            }

            val builder = MaterialAlertDialogBuilder(requireContext())

            val dialog = builder.setTitle("리뷰 저장")
                .setMessage("작성한 리뷰를 저장하시겠습니까?")
                .setPositiveButton("확인") { dialog, _ ->
                    fragmentWriteProductReviewBinding.buttonWriteProductReviewSaveReview.visibility = View.GONE
                    CoroutineScope(Dispatchers.Main).launch {
                        progressBarInWriteReview.visibility = View.VISIBLE
                        try {
                            if (photoList.isNotEmpty()) {
                                showToast("사진 업로드 중입니다...")

                                // 사진 업로드
                                uploadPhotosToFirebase { success ->
                                    if (success) {
                                        saveReviewData(reviewContent, reviewRating, reviewTimestamp, reviewDate)
                                    } else {
                                        showToast("사진 업로드 실패")
                                    }
                                }
                            } else {
                                saveReviewData(reviewContent, reviewRating, reviewTimestamp, reviewDate)
                            }
                        } catch (e: Exception) {
                            Log.e("test100", "리뷰 저장 중 오류 발생", e)
                            showToast("리뷰 저장 실패: ${e.message}")
                        } finally {
                            dialog.dismiss()
                        }
                    }
                }
                .setNegativeButton("취소") { dialog, _ -> dialog.dismiss() }
                .setCancelable(true)
                .create()

            dialog.window?.setBackgroundDrawableResource(android.R.color.white)
            dialog.show()
        }
    }

    private fun saveReviewData(
        reviewContent: String?,
        reviewRating: Float?,
        reviewTimestamp: Long,
        reviewDate: String
    ) {
        val reviewModel = ReviewModel().also {
            it.reviewContent = reviewContent!!
            it.reviewRating = reviewRating ?: 0f
            it.reviewTimeStamp = reviewTimestamp
            it.reviewWriteDate = reviewDate
            it.reviewPhoto = photoFileNameList // 업로드된 사진 파일명 추가 (없으면 빈 리스트)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val reviewDocumentID = ReviewService.addReviewData(reviewModel)

            val work = async {
                ReviewService.addReviewDataInProduct(reviewDocumentID, "8DynxD5PPXsWOYPXQFmF")
            }
            work.join()

            withContext(Dispatchers.Main) {
                showToast("리뷰가 저장되었습니다.")
                mainFragment.removeFragment(ShopSubFragmentName.WRITE_PRODUCT_REVIEW_FRAGMENT)
                fragmentWriteProductReviewBinding.progressBarInWriteReview.visibility = View.GONE
            }
        }
    }





    // 현재 날짜를 반환하는 함수
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }


    private suspend fun uploadPhotosToFirebase(onComplete: (Boolean) -> Unit) {
        withContext(Dispatchers.IO) {
            try {
                photoList.forEachIndexed { index, uri ->
                    val serverFilePath = "review_photos/photo_${System.currentTimeMillis()}_$index.jpg"
                    photoFileNameList.add(serverFilePath) // 파일명을 리스트에 추가

                    // URI를 직접 Firebase Storage에 업로드
                    ReviewService.uploadImageFromUri(uri, serverFilePath)
                    Log.d("uploadPhotosToFirebase", "사진 업로드 완료: $serverFilePath")
                }
                withContext(Dispatchers.Main) {
                    onComplete(true)
                }
            } catch (e: Exception) {
                Log.e("uploadPhotosToFirebase", "사진 업로드 실패", e)
                withContext(Dispatchers.Main) {
                    onComplete(false)
                }
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

        inner class PhotoViewHolder(val binding: ItemPhotoBinding) :
            RecyclerView.ViewHolder(binding.root)
    }


}
