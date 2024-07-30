package campus.tech.kakao.map.presentation.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityKakaoMapViewBinding
import campus.tech.kakao.map.presentation.viewmodel.KakaoMapViewModel
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class KakaoMapViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKakaoMapViewBinding
    private var kakaoMap: KakaoMap? = null
    private val viewModel: KakaoMapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kakao_map_view)
        binding.kakaoMap = this
        binding.lifecycleOwner = this
        binding.kakaoMapViewModel = viewModel

        binding.persistentBottomSheet.visibility = View.GONE

        initKakaoMap()
        clickSearchButton()
    }

    private fun initKakaoMap() {

        binding.mapView.start(object : MapLifeCycleCallback() {
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
                prepareMap()
            }
        })
    }
    private fun clickSearchButton() {
        binding.searchButton.setOnClickListener {
            Intent(this, SearchActivity::class.java).let {
                startActivity(it)
            }
        }
    }

    private fun prepareMap() {
        kakaoMap?.let { map ->
            setLabel(map)
            moveCamera(map)
        }
    }

    private fun setLabel(map: KakaoMap) {
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
            binding.persistentBottomSheet.visibility = View.GONE
            layer?.hideAllLabel()
        } else {
            layer?.showAllLabel()
            binding.persistentBottomSheet.visibility = View.VISIBLE
        }
    }

    private fun moveCamera(map: KakaoMap) {
        val position = LatLng.from(
            viewModel.yCoordinate.value ?: 37.402005,
            viewModel.xCoordinate.value ?: 127.108621
        )
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(position)
        map.moveCamera(cameraUpdate, CameraAnimation.from(500, true, true))
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.pause()
    }
}