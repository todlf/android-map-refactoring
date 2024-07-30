package campus.tech.kakao.map.data.repository

import android.content.SharedPreferences
import campus.tech.kakao.map.data.local.map.KakaoMapData
import campus.tech.kakao.map.data.local.map.MapConstants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KakaoMapRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun saveKakaoMapReadyData(x: Double, y: Double, name: String, address: String) {
        with(sharedPreferences.edit()) {
            putString(MapConstants.KEY_X_COORDINATE, x.toString())
            putString(MapConstants.KEY_Y_COORDINATE, y.toString())
            putString(MapConstants.KEY_NAME, name)
            putString(MapConstants.KEY_ADDRESS, address)
            apply()
        }
    }

    fun getKakaoMapReadyData(): KakaoMapData {
        val xCoordinate = sharedPreferences.getString(MapConstants.KEY_X_COORDINATE, "127.108621")
            ?.toDoubleOrNull() ?: 127.108621
        val yCoordinate = sharedPreferences.getString(MapConstants.KEY_Y_COORDINATE, "37.402005")
            ?.toDoubleOrNull() ?: 37.402005
        val name = sharedPreferences.getString(MapConstants.KEY_NAME, "이름") ?: "이름"
        val address = sharedPreferences.getString(MapConstants.KEY_ADDRESS, "주소") ?: "주소"

        return KakaoMapData(xCoordinate, yCoordinate, name, address)
    }
}