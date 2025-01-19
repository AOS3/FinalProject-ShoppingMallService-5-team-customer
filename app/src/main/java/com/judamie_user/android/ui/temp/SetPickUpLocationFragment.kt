package com.judamie_user.android.ui.temp

import android.Manifest
import android.content.Context
import android.content.DialogInterface
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
import androidx.core.content.ContextCompat.getSystemService
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
import com.judamie_user.android.viewmodel.temp.SetPickUpLocationViewModel
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import java.io.IOException
import java.util.Locale
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.judamie_user.android.databinding.FragmentShowPickupLocationDialogBinding
import com.judamie_user.android.viewmodel.componentviewmodel.ShowPickupLocationDialogViewModel


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

    // 임시 픽업지 주소 리스트
    val addressList = listOf(
        "서울특별시 종로구 세종대로 175",
        "서울특별시 강남구 테헤란로 123",
        "충청북도 청주시 흥덕구 오송읍 오송생명5로 184-4",
        "03900, 서울 마포구 하늘공원로 84 (상암동)",
        "155 W Center Street Promenade, Anaheim, CA 92805",
        "1159 N Rengstorff Ave, Mountain View, CA 94043",
        "1600 Amphitheatre Pkwy, Mountain View, CA 94043",
        "2000 N Shoreline Blvd Ground Floor, Mountain View, CA 94043"
    )

    val addressMap = mapOf(
        "A픽업지" to "1600 Amphitheatre Pkwy, Mountain View, CA 94043",
        "B픽업지" to "2000 N Shoreline Blvd Ground Floor, Mountain View, CA 94043"

    )

    // 마커와 주소를 매핑하는 맵
    private val markerAddressMap = HashMap<Marker, String>()


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

        return fragmentSetPickUpLocationBinding.root
    }

    fun movePrevFragment() {
        mainFragment.removeFragment(ShopSubFragmentName.SET_PICKUP_LOCATION_FRAGMENT)
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
        mapFragment.getMapAsync {
            mainGoogleMap = it
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

            val gpsSavedLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val networkSavedLocation =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            val passiveSavedLocation =
                locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)

            if (gpsSavedLocation != null) {
                setMyLocation(gpsSavedLocation)
            } else if (networkSavedLocation != null) {
                setMyLocation(networkSavedLocation)
            } else if (passiveSavedLocation != null) {
                setMyLocation(passiveSavedLocation)
            }

            // 주소 리스트의 모든 주소를 처리

            addressMap.forEach{
                addMarker(it.value)
            }

//            addressList.forEach { address ->
//                addMarker(address)
//            }


            // 카메라가 이동을 멈춘 후 호출되는 리스너
            mainGoogleMap.setOnCameraIdleListener {
                val centerLatLng = mainGoogleMap.cameraPosition.target
                addCenterMarker(centerLatLng) // 지도 중심에 마커 추가
            }
        }
    }

    fun addCenterMarker(centerLatLng: LatLng): String? {
        // 기존 중심 마커 제거
        centerMarker?.remove()

        // 새로운 중심 마커 옵션 설정
        val markerOptions = MarkerOptions()
            .position(centerLatLng)
            .title("선택한 위치")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)) // 마커 색상

        // VectorDrawable을 Bitmap으로 변환하여 마커에 적용
        val markerBitmap = vectorToBitmap(requireContext(), R.drawable.red_marker)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(markerBitmap))

        // 마커 추가
        centerMarker = mainGoogleMap.addMarker(markerOptions)

        // 위치에 대한 주소 표시
        val geocoder = Geocoder(requireContext(), Locale.KOREAN) // 한국어 설정
        return try {
            val addresses = geocoder.getFromLocation(centerLatLng.latitude, centerLatLng.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0].getAddressLine(0) // 한글 주소


                fragmentSetPickUpLocationBinding.setPickUpLocationViewModel?.textViewSetPickUpLocationCenterAddressText?.value = address
                Log.d("CenterMarker", "한글 주소: $address")
                address // 반환
            } else {
                Log.e("CenterMarker", "주소를 찾을 수 없습니다.")
                null
            }
        } catch (e: IOException) {
            Log.e("Geocoder", "Geocoding 에러: $e")
            null
        }
    }

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

    // 지도에 마커 추가하는 메서드
    fun addMarker(address: String) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            // 주소를 위도와 경도로 변환
            val addressList = geocoder.getFromLocationName(address, 1)
            if (addressList != null && addressList.isNotEmpty()) {
                val location = addressList[0]
                val latLng = LatLng(location.latitude, location.longitude)

                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title(address)

                // VectorDrawable을 Bitmap으로 변환하여 마커에 적용
                val markerBitmap = vectorToBitmap(requireContext(), R.drawable.locationmark)
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(markerBitmap))

                // 마커 추가 및 저장
                val marker = mainGoogleMap.addMarker(markerOptions)
                if (marker != null) {
                    markerAddressMap[marker] = address // 마커와 주소를 매핑
                }

                // 마커 클릭 리스너 설정
                setupMarkerClickListener(mainGoogleMap)
            } else {
                Log.e("Geocoder", "주소를 찾을 수 없습니다: $address")
            }
        } catch (e: IOException) {
            Log.e("Geocoder", "Geocoder 사용 중 오류 발생", e)
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
        Log.d("test", "onMarkerClick")

        // 마커에 매핑된 주소 가져오기
        val address = markerAddressMap[marker]
        if (address != null) {
            Log.d("test", "클릭된 마커의 주소: $address")

            val binding: FragmentShowPickupLocationDialogBinding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_show_pickup_location_dialog,
                null,
                false
            )

            //뷰모델 객체를 만든다
            val viewModel = ShowPickupLocationDialogViewModel()
            binding.showPickupLocationDialogViewModel = viewModel
            binding.lifecycleOwner = this

            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setView(binding.root)
                .create()

            // 클릭리스너의 동작을 설정한다
            viewModel.onCancelClick = {
                dialog.dismiss()
            }

            viewModel.onSelectClick = {
                // Handle "Select" action here
                fragmentSetPickUpLocationBinding.setPickUpLocationViewModel?.textViewSetPickUpLocationNameText?.value = address
                dialog.dismiss()
            }

            viewModel.onCallClick = {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$01012345678")
                }
                startActivity(intent)
            }

            viewModel.pickupLocationNameText.value = "cu"
            viewModel.pickupLocationDetailText.value = address

            // Show the dialog
            dialog.show()
        } else {
            Log.e("test", "주소를 찾을 수 없습니다.")
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

    fun callToPickupLocation() {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$01012345678")
        }
        startActivity(intent)
    }

    fun pickUpLocationShowInfo() {

    }


}