package com.judamie_user.android.viewmodel.rowviewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.judamie_user.android.R
import com.judamie_user.android.ui.subfragment.ShowUserOrderListFragment

class RowOrderListViewModel(val showUserOrderListFragment: ShowUserOrderListFragment):ViewModel() {
    // textViewOrderDate
    val textViewOrderDateText = MutableLiveData("")

    //imageViewOrderImage
    // Glide를 사용할 URI LiveData 추가
    val imageViewOrderImageUri = MutableLiveData<Uri>()

    //textViewPickupState - textColor
    val textViewPickupStateColor = MutableLiveData<Int>()

    //textViewOrderName
    val textViewOrderNameText = MutableLiveData("")

    //textViewCountMore
    val textViewCountMoreText = MutableLiveData("")

    //textViewTotalPrice
    val textViewTotalPrice = MutableLiveData("")

    //textViewOrderCount
    val textViewOrderCount = MutableLiveData("")

    //textViewPickupState
    val textViewPickupState = MutableLiveData("")
}