package campus.tech.kakao.map.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class KakaoMapViewModel @Inject constructor(
    @ApplicationContext private val context: Context
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
        getData()
    }

    fun saveCoordinates(x: Double, y: Double) {
        val sharedPref = context.getSharedPreferences("Coordinates", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("xCoordinate", x.toString())
            putString("yCoordinate", y.toString())
            apply()
        }
        _xCoordinate.value = x
        _yCoordinate.value = y
    }

    fun saveToBottomSheet(name: String, address: String) {
        val sharedPref = context.getSharedPreferences("BottomSheet", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("name", name)
            putString("address", address)
            apply()
        }
        _name.value = name
        _address.value = address
    }

    fun getData() {
        val sharedPref = context.getSharedPreferences("Coordinates", Context.MODE_PRIVATE)
        _xCoordinate.value =
            sharedPref.getString("xCoordinate", "127.108621")?.toDoubleOrNull() ?: 127.108621
        _yCoordinate.value =
            sharedPref.getString("yCoordinate", "37.402005")?.toDoubleOrNull() ?: 37.402005

        val sharedPref1 = context.getSharedPreferences("BottomSheet", Context.MODE_PRIVATE)
        _name.value = sharedPref1.getString("name", "이름") ?: "이름"
        _address.value = sharedPref1.getString("address", "주소") ?: "주소"
    }
}