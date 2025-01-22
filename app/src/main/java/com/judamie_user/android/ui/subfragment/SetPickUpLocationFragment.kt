package com.judamie_user.android.ui.subfragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.LOCATION_SERVICE
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentSetPickUpLocationBinding
import com.judamie_user.android.ui.fragment.MainFragment
import com.judamie_user.android.ui.fragment.ShopSubFragmentName
import com.judamie_user.android.viewmodel.fragmentviewmodel.SetPickUpLocationViewModel
import com.google.android.gms.maps.SupportMapFragment
import java.io.IOException
import java.util.Locale
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Base64
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.judamie_user.android.databinding.DialogSettingPickupLocationBinding
import com.judamie_user.android.databinding.DialogShowPickupLocationInformationBinding
import com.judamie_user.android.firebase.model.PickupLocationModel
import com.judamie_user.android.firebase.service.CouponService
import com.judamie_user.android.firebase.service.PickupLocationService
import com.judamie_user.android.viewmodel.componentviewmodel.SettingPickupLocationDialogViewModel
import com.judamie_user.android.viewmodel.componentviewmodel.ShowPickupLocationInformationDialogViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest


class SetPickUpLocationFragment(val mainFragment: MainFragment) : Fragment() {
    lateinit var fragmentSetPickUpLocationBinding: FragmentSetPickUpLocationBinding

    // 권한 확인을 위한 런처
    lateinit var permissionCheckLauncher: ActivityResultLauncher<Array<String>>

    // 위치 정보 관리 객체
    lateinit var locationManager: LocationManager

    // 위치 측정을 하면 반응하는 리스너
    lateinit var myLocationListener: MyLocationListener

    // 사용자의 현재 위치를 담을 변수
    var userLocation: Location? = null

    // 구글 지도 객체를 담을 변수를 선언한다.
    lateinit var mainGoogleMap: GoogleMap

    // 현재 사용자의 위치를 표시할 마커 객체
    var myMarker: Marker? = null

    // 현재 사용자가 보고있는 지도의 중심마커
    var centerMarker: Marker? = null

    // okHttp3 객체생성
    val client = OkHttpClient()



    // 임시 픽업지 주소 리스트
    val addressList = listOf(
        "서울특별시 종로구 세종대로 175",
        "서울특별시 강남구 테헤란로 123",
        "충청북도 청주시 흥덕구 오송읍 오송생명5로 184-4",
        "서울 마포구 서강로 97 삼성아파트단지 상가 114호",
        "충북 청주시 흥덕구 오송읍 오송가락로 123 3층 305호"
    )

    val pickupAddress = mutableListOf<PickupLocationModel>()

    // 마커와 주소를 매핑하는 맵
    private val markerAddressMap = HashMap<Marker, String>()

    // 마커와 pickupLocationModel을 매핑하는 맵
    private var markerModelMap = HashMap<Marker, PickupLocationModel>()


    // 확인할 권한 목록
    val permissionList = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSetPickUpLocationBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_set_pick_up_location,
            container,
            false
        )
        fragmentSetPickUpLocationBinding.setPickUpLocationViewModel =
            SetPickUpLocationViewModel(this)
        fragmentSetPickUpLocationBinding.lifecycleOwner = viewLifecycleOwner
        // MapsInitializer 호출
        MapsInitializer.initialize(requireContext(), MapsInitializer.Renderer.LATEST, null)

        // 권한 확인 런처 초기화
        createPermissionCheckLauncher() // 먼저 초기화

        // 권한 확인을 위한 런처 실행
        permissionCheckLauncher.launch(permissionList) // 초기화 후 실행

        // 구글 맵 설정
        settingGoogleMap()

        setupCustomZoomControls()

        return fragmentSetPickUpLocationBinding.root
    }

    fun movePrevFragment() {
        mainFragment.removeFragment(ShopSubFragmentName.SET_PICKUP_LOCATION_FRAGMENT)
    }
    private fun setupCustomZoomControls() {
        val zoomInButton = fragmentSetPickUpLocationBinding.root.findViewById<ImageButton>(R.id.zoomInButton)
        val zoomOutButton = fragmentSetPickUpLocationBinding.root.findViewById<ImageButton>(R.id.zoomOutButton)

        zoomInButton.setOnClickListener {
            mainGoogleMap.animateCamera(CameraUpdateFactory.zoomIn())
        }

        zoomOutButton.setOnClickListener {
            mainGoogleMap.animateCamera(CameraUpdateFactory.zoomOut())
        }
    }


    // 위치 측정에 성공하면 동작하는 리스너
    inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("test100", "위도 : ${location.latitude}, 경도 : ${location.longitude}")
            setMyLocation(location)
        }
    }

    // 지도에 현재 위치를 표시하는 메서드
    fun setMyLocation(location: Location) {
        // 사용자의 현재 위치를 변수에 담아준다.
        userLocation = location
        // 현재 위치 측정을 중단한다.
        locationManager.removeUpdates(myLocationListener)

        // 위도와 경도를 관리하는 객체를 생성한다.
        val loc1 = LatLng(location.latitude, location.longitude)

        // 지도를 업데이트할 수 있는 객체를 생성한다.
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(loc1, 15.0f)
        mainGoogleMap.animateCamera(cameraUpdate)

        val markerOptions = MarkerOptions().position(loc1)

        // 기존 마커 제거
        myMarker?.remove()
        myMarker = null

        // VectorDrawable을 Bitmap으로 변환하여 마커에 적용
        val markerBitmap = vectorToBitmap(requireContext(), R.drawable.my_location_24px) // 벡터 리소스 ID
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(markerBitmap))

        // 새 마커 추가
        myMarker = mainGoogleMap.addMarker(markerOptions)
    }

    // 구글 맵에 대한 설정을 하는 함수
    fun settingGoogleMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragmentSetPickupLocation) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mainGoogleMap = googleMap
            mainGoogleMap.uiSettings.isZoomControlsEnabled = false
            mainGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            locationManager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
            myLocationListener = MyLocationListener()

            // 권한 확인
            val check1 = ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            val check2 = ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            if (check1 == PackageManager.PERMISSION_DENIED || check2 == PackageManager.PERMISSION_DENIED) {
                permissionCheckLauncher.launch(permissionList)
                return@getMapAsync
            }

            val gpsSavedLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val networkSavedLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            val passiveSavedLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)

            if (gpsSavedLocation != null) {
                setMyLocation(gpsSavedLocation)
            } else if (networkSavedLocation != null) {
                setMyLocation(networkSavedLocation)
            } else if (passiveSavedLocation != null) {
                setMyLocation(passiveSavedLocation)
            }

            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO) {
                    PickupLocationService.gettingAllPickLocations() // 모든 픽업지 데이터를 가져오기
                }
                val allPickupLocations = work1.await()
                pickupAddress.addAll(allPickupLocations)
                pickupAddress.forEach {
                    Log.d("test",it.pickupLocStreetAddress)
                    Log.d("test",it.pickupLocAddressDetail)
                }
                // 코루틴을 사용하여 addressList의 각 주소에 대해 마커 추가
                lifecycleScope.launch {
                    pickupAddress.forEach {
                        withContext(Dispatchers.IO) {
                            try {
                                addMarkerOnMap(it)
                            } catch (e: Exception) {
                                Log.e("settingGoogleMap", "Failed to add marker for address $it: ${e.message}")
                            }
                        }
                    }
                }
            }




            // 카메라가 이동을 멈춘 후 호출되는 리스너
            mainGoogleMap.setOnCameraIdleListener {
                val centerLatLng = mainGoogleMap.cameraPosition.target
                addCenterMarkerKakao(centerLatLng) // 지도 중심에 마커 추가
            }
        }
    }


//    fun addCenterMarker(centerLatLng: LatLng): String? {
//        // 기존 중심 마커 제거
//        centerMarker?.remove()
//
//        // 마커 옵션 설정
//        val markerOptions = MarkerOptions()
//            .position(centerLatLng)
//            .title("선택한 위치")
//            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//
//        // 커스텀 마커 아이콘 설정
//        val markerBitmap = vectorToBitmap(requireContext(), R.drawable.red_marker)
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(markerBitmap))
//        centerMarker = mainGoogleMap.addMarker(markerOptions)
//
//        // Geocoder 사용
//        val geocoder = Geocoder(requireContext(), Locale.KOREAN)
//        return try {
//            val addresses = geocoder.getFromLocation(centerLatLng.latitude, centerLatLng.longitude, 1)
//
//            if (!addresses.isNullOrEmpty()) {
//                val thoroughfare = addresses[0].thoroughfare // 도로명 주소
//                val subThoroughfare = addresses[0].subThoroughfare // 건물 번호
//                val roadNameAddress = if (thoroughfare != null && subThoroughfare != null) {
//                    "$thoroughfare $subThoroughfare" // 도로명 주소와 건물 번호를 조합
//                } else {
//                    thoroughfare ?: addresses[0].getAddressLine(0) // 도로명 주소 또는 전체 주소
//                }
//
//                // ViewModel에 주소 설정
//                fragmentSetPickUpLocationBinding.setPickUpLocationViewModel?.textViewSetPickUpLocationCenterAddressText?.value = roadNameAddress
//                Log.d("CenterMarker", "도로명 주소: $roadNameAddress")
//                roadNameAddress // 반환
//            } else {
//                Log.e("CenterMarker", "주소를 찾을 수 없습니다.")
//                null
//            }
//        } catch (e: IOException) {
//            Log.e("Geocoder", "Geocoding 에러: $e")
//            null
//        }
//    }



    // 권한 확인을 위해 사용할 런처 생성
    fun createPermissionCheckLauncher() {
        // 런처를 등록한다.
        permissionCheckLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
                // 모든 권한에 대해 확인한다.
                permissionList.forEach { permissionName ->
                    // 현재 권한이 허용되어 있지 않다면 다이얼로그를 띄운다.
                    if (result[permissionName] == false) {
                        // 설정 화면을 띄우는 메서드를 호출한다.
                        startSettingActivity()
                        // 함수 종료
                        return@registerForActivityResult
                    }
                }
            }
    }

    // 애플리케이션의 설정화면을 실행시키는 메서드
    fun startSettingActivity() {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        materialAlertDialogBuilder.setTitle("권한 확인 요청")
        materialAlertDialogBuilder.setMessage("권한을 모두 허용해줘야 정상적은 서비스 이용이 가능합니다")
        materialAlertDialogBuilder.setPositiveButton("설정 화면으로 가기") { _, _ ->
            // 앱 설정 화면으로 이동
            val uri = Uri.fromParts("package", requireContext().packageName, null)
            val permissionIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
            permissionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(permissionIntent)
        }

        // 다이얼로그 띄우고, 앱이 종료되지 않도록
        materialAlertDialogBuilder.setCancelable(false)
        materialAlertDialogBuilder.show()
    }

    // 현재 위치를 측정하는 메서드
    fun getMyLocation() {
        // 권한 허용 여부 확인
        val check1 = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val check2 = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        // 권한이 없다면 권한 요청
        if (check1 == PackageManager.PERMISSION_DENIED || check2 == PackageManager.PERMISSION_DENIED) {
            permissionCheckLauncher.launch(permissionList)
            return
        }

        // GPS 프로바이더 사용이 가능하다면
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0.0f,
                myLocationListener
            )
        }
        // Network 프로바이더 사용이 가능하다면
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0,
                0.0f,
                myLocationListener
            )
        }
    }


    // 마커 클릭하면 다이얼로그 띄워주는 메서드
    fun setupMarkerClickListener(mainGoogleMap: GoogleMap) {
        mainGoogleMap.setOnMarkerClickListener { marker ->
            // 클릭된 마커에 대한 작업 수행
            onMarkerClick(marker)
            true // true로 반환하면 기본 동작(카메라 이동)을 막습니다.
        }
    }

    fun onMarkerClick(marker: Marker) {
        val pickupLocationModel = markerModelMap[marker]
        if (pickupLocationModel != null) {
            val binding: DialogSettingPickupLocationBinding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.dialog_setting_pickup_location,
                null,
                false
            )

            val viewModel = SettingPickupLocationDialogViewModel()
            binding.settingPickupLocationDialogViewModel = viewModel
            binding.lifecycleOwner = this

            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setView(binding.root)
                .create()

            viewModel.textViewDialogSettingPickupLocationNameText?.value = pickupLocationModel.pickupLocName
            viewModel.textViewDialogSettingPickupLocationStreetAddressText?.value = pickupLocationModel.pickupLocStreetAddress
            viewModel.textViewDialogSettingPickupLocationAddressDetailText?.value = pickupLocationModel.pickupLocAddressDetail
            viewModel.textViewDialogSettingPickupLocationPhoneNumberText?.value = pickupLocationModel.pickupLocPhoneNumber
            viewModel.textViewDialogSettingPickupLocationDetailText?.value = pickupLocationModel.pickupLocInfomation

            viewModel.onCloseClick = {
                dialog.dismiss()
            }

            viewModel.onChoiceClick = {
                fragmentSetPickUpLocationBinding.setPickUpLocationViewModel?.apply {
                    selectedPickupLocation.value = pickupLocationModel // 선택된 위치 설정
                    textViewSetPickUpLocationNameText.value = pickupLocationModel.pickupLocName
                }
                dialog.dismiss()
            }

            viewModel.onCallClick = {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${pickupLocationModel.pickupLocPhoneNumber}")
                }
                startActivity(intent)
            }

            dialog.show()
        } else {
            Log.e("onMarkerClick", "No matching model found for marker.")
        }
    }



    fun vectorToBitmap(context: Context, vectorResId: Int): Bitmap {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId) as Drawable
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    fun pickUpLocationShowInfo(pickupLocationModel: PickupLocationModel) {
        val binding: DialogShowPickupLocationInformationBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_show_pickup_location_information,
            null,
            false
        )

        val viewModel = ShowPickupLocationInformationDialogViewModel()
        binding.showPickupLocationInformationDialogViewModel = viewModel
        binding.lifecycleOwner = this

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .create()

        viewModel.textViewDialogShowPickupLocationInformationNameText?.value = pickupLocationModel.pickupLocName
        viewModel.textViewDialogShowPickupLocationInformationStreetAddressText?.value = pickupLocationModel.pickupLocStreetAddress
        viewModel.textViewDialogShowPickupLocationInformationAddressDetailText?.value = pickupLocationModel.pickupLocAddressDetail
        viewModel.textViewDialogShowPickupLocationInformationPhoneNumberText?.value = pickupLocationModel.pickupLocPhoneNumber
        viewModel.textViewDialogShowPickupLocationInformationDetailText?.value = pickupLocationModel.pickupLocInfomation

        viewModel.onCallClick = {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${pickupLocationModel.pickupLocPhoneNumber}")
            }
            startActivity(intent)
        }

        viewModel.onCloseClick = {
            dialog.dismiss()
        }

        dialog.show()
    }



    suspend fun addMarkerOnMap(pickupLocation: PickupLocationModel) {
        val address = "${pickupLocation.pickupLocStreetAddress} ${pickupLocation.pickupLocAddressDetail}"
        val kakaoApiKey = requireContext().getString(R.string.KAKAO_API_KEY)

        val url = "https://dapi.kakao.com/v2/local/search/address.json?query=$address"

        val request = Request.Builder()
            .url(url)
            .header("Authorization", "KakaoAK $kakaoApiKey")
            .build()

        try {
            val response = withContext(Dispatchers.IO) {
                client.suspendCall(request)
            }

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val jsonObject = JSONObject(responseBody ?: "")
                val documents: JSONArray = jsonObject.getJSONArray("documents")

                if (documents.length() > 0) {
                    val firstResult = documents.getJSONObject(0)
                    val longitude = firstResult.getString("x").toDouble()
                    val latitude = firstResult.getString("y").toDouble()

                    withContext(Dispatchers.Main) {
                        val latLng = LatLng(latitude, longitude)
                        val markerOptions = MarkerOptions()
                            .position(latLng)
                            .title(address)

                        val markerBitmap = vectorToBitmap(requireContext(), R.drawable.locationmark)
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(markerBitmap))

                        val marker = mainGoogleMap.addMarker(markerOptions)
                        if (marker != null) {
                            markerModelMap[marker] = pickupLocation // 모델 매핑
                        }

                        setupMarkerClickListener(mainGoogleMap)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("SetPickUpLocationFragment", "Failed to make request: ${e.message}")
        }
    }



    // OkHttp 클라이언트를 코루틴에서 사용하기 위한 확장 함수
    suspend fun OkHttpClient.suspendCall(request: Request): Response {
        return suspendCancellableCoroutine { continuation ->
            val call = newCall(request)
            continuation.invokeOnCancellation { call.cancel() }
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    if (continuation.isActive) {
                        continuation.resumeWith(Result.failure(e))
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    continuation.resumeWith(Result.success(response))
                }
            })
        }
    }

    fun addCenterMarkerKakao(centerLatLng: LatLng): String? {
        // 기존 중심 마커 제거
        centerMarker?.remove()

        // 마커 옵션 설정
        val markerOptions = MarkerOptions()
            .position(centerLatLng)
            .title("선택한 위치")

        // 커스텀 마커 아이콘 설정
        val markerBitmap = vectorToBitmap(requireContext(), R.drawable.red_marker)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(markerBitmap))
        centerMarker = mainGoogleMap.addMarker(markerOptions)

        val kakaoApiKey = requireContext().getString(R.string.KAKAO_API_KEY)
        val url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x=${centerLatLng.longitude}&y=${centerLatLng.latitude}&input_coord=WGS84"

        val request = Request.Builder()
            .url(url)
            .header("Authorization", "KakaoAK $kakaoApiKey")
            .build()

        try {
            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) {
                    client.suspendCall(request)
                }

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val jsonObject = JSONObject(responseBody ?: "")
                    val documents: JSONArray = jsonObject.getJSONArray("documents")

                    if (documents.length() > 0) {
                        val firstResult = documents.getJSONObject(0)
                        val roadAddress = firstResult.optJSONObject("road_address")
                        val addressName = roadAddress?.getString("address_name") ?: "주소를 찾을 수 없습니다."

                        // ViewModel에 주소 설정
                        fragmentSetPickUpLocationBinding.setPickUpLocationViewModel?.textViewSetPickUpLocationCenterAddressText?.value = addressName
                        Log.d("CenterMarkerKakao", "도로명 주소: $addressName")
                    } else {
                        Log.e("CenterMarkerKakao", "도로명 주소를 찾을 수 없습니다.")
                    }
                } else {
                    Log.e("CenterMarkerKakao", "Kakao API 요청 실패: ${response.code}")
                }
            }
        } catch (e: Exception) {
            Log.e("CenterMarkerKakao", "Kakao API 호출 에러: ${e.message}")
        }

        return null // 비동기로 실행되므로 즉각 반환할 값은 없음
    }
}