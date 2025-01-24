package com.judamie_user.android.ui.component

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.judamie_user.android.R
import com.judamie_user.android.databinding.FragmentCartDialogBinding
import com.judamie_user.android.databinding.FragmentShowPickupLocationDialogBinding
import com.judamie_user.android.viewmodel.componentviewmodel.CartViewModel
import com.judamie_user.android.viewmodel.componentviewmodel.ShowPickupLocationDialogViewModel

class CartDialogFragment(
    private val title: String?,
    private val content: String?,
) : DialogFragment() {

    lateinit var fragmentCartDialogBinding: FragmentCartDialogBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentCartDialogBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_cart_dialog,container,false)
        fragmentCartDialogBinding.cartViewModel = CartViewModel(this)
        fragmentCartDialogBinding.lifecycleOwner = viewLifecycleOwner


        // 다이얼로그 배경을 투명하게 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        // 데이터 바인딩을 통해 뷰와 데이터 연결
        fragmentCartDialogBinding.cartViewModel?.apply {

            cartTitleText.value = title
            cartDetailText.value = content

        }

        return fragmentCartDialogBinding.root
    }

    // 다이얼로그 닫기 버튼 메서드
    fun buttonClose(){
        dismiss()
    }
}