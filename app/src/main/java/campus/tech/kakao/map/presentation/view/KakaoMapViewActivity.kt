package campus.tech.kakao.map.presentation.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import campus.tech.kakao.map.R
import campus.tech.kakao.map.presentation.viewmodel.KakaoMapViewModel
import campus.tech.kakao.map.presentation.viewmodel.KakaoMapViewModelFactory
import com.kakao.sdk.common.util.Utility
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class KakaoMapViewActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var searchButton: Button
    private lateinit var placeName: TextView
    private lateinit var placeAddress: TextView
    private lateinit var persistentBottomSheet: LinearLayout

    private var kakaoMap: KakaoMap? = null

    private val viewModel: KakaoMapViewModel by viewModels {
        KakaoMapViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kakao_map_view)

        observeKakaoMapViewModel()
        initKakaoMap()

        searchButton = findViewById(R.id.searchButton)
        placeName = findViewById(R.id.placeName)
        placeAddress = findViewById(R.id.placeAddress)
        persistentBottomSheet = findViewById(R.id.persistent_bottom_sheet)
        persistentBottomSheet.visibility = View.GONE

        clickSearchButton()
    }


    private fun initKakaoMap() {

        mapView = findViewById(R.id.mapView)
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.e("KakaoMapViewActivity", "Map destroyed")
            }

            override fun onMapError(error: Exception) {
                startActivity(Intent(this@KakaoMapViewActivity, MapErrorActivity::class.java))
                Log.e("KakaoMapViewActivity", "Map error: ${error.message}")
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                this@KakaoMapViewActivity.kakaoMap = kakaoMap
                mapReady()
            }
        })
    }

    private fun observeKakaoMapViewModel() {
        viewModel.name.observe(this, Observer { name ->
            placeName.text = name
            mapReady()
        })

        viewModel.address.observe(this, Observer { address ->
            placeAddress.text = address
            mapReady()
        })
    }

    private fun clickSearchButton() {
        searchButton.setOnClickListener {
            Intent(this, SearchActivity::class.java).let {
                startActivity(it)
            }
        }
    }

    private fun mapReady() {
        kakaoMap?.let { map ->
            setLabel(map)
            moveCamera(map)
        }
    }

    private fun setLabel(map: KakaoMap){
        val position = LatLng.from(
            viewModel.yCoordinate.value ?: 37.402005,
            viewModel.xCoordinate.value ?: 127.108621
        )

        val style = map.labelManager?.addLabelStyles(
            LabelStyles.from(
                LabelStyle.from(R.drawable.kakaomap_logo).setTextStyles(30, Color.BLUE)
            )
        )

        val options: LabelOptions = LabelOptions.from(position).setStyles(style)

        val layer = map.labelManager?.layer
        layer?.addLabel(options)?.changeText(viewModel.name.value ?: "이름")

        if (viewModel.name.value == "이름") {
            persistentBottomSheet.visibility = View.GONE
            layer?.hideAllLabel()
        } else {
            layer?.showAllLabel()
            persistentBottomSheet.visibility = View.VISIBLE
        }
    }

    private fun moveCamera(map: KakaoMap){
        val position = LatLng.from(
            viewModel.yCoordinate.value ?: 37.402005,
            viewModel.xCoordinate.value ?: 127.108621
        )
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(position)
        map.moveCamera(cameraUpdate, CameraAnimation.from(500, true, true))
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }
}