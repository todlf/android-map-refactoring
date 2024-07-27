package campus.tech.kakao.map.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class KakaoMapViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KakaoMapViewModel::class.java)) {
            return KakaoMapViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}