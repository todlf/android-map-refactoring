package campus.tech.kakao.map

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.util.Utility
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KakaoMapApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // onMapError 호출하기
        //KakaoMapSdk.init(this, "dfsfdsdsdasfds")

        val key = getString(R.string.kakao_api_key)
        KakaoMapSdk.init(this, key)

    }
}