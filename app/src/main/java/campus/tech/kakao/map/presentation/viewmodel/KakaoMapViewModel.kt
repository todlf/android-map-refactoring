package campus.tech.kakao.map.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.data.repository.KakaoMapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KakaoMapViewModel @Inject constructor(
    private val repository: KakaoMapRepository
) : ViewModel() {

    private val _xCoordinate = MutableLiveData<Double>()
    val xCoordinate: LiveData<Double> get() = _xCoordinate

    private val _yCoordinate = MutableLiveData<Double>()
    val yCoordinate: LiveData<Double> get() = _yCoordinate

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> get() = _address

    init {
        loadKakaoMapReadyData()
    }

    fun saveKakaoMapReadyData(x: Double, y: Double, name: String, address: String) {
        repository.saveKakaoMapReadyData(x, y, name, address)
        _xCoordinate.value = x
        _yCoordinate.value = y
        _name.value = name
        _address.value = address
    }

    fun loadKakaoMapReadyData() {
        val data = repository.getKakaoMapReadyData()
        _xCoordinate.value = data.xCoordinate
        _yCoordinate.value = data.yCoordinate
        _name.value = data.name
        _address.value = data.address

    }
}