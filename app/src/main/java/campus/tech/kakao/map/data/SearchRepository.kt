package campus.tech.kakao.map.data

import android.util.Log
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.domain.model.SavedSearch
import campus.tech.kakao.map.domain.model.SearchData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(
    private val searchDataDao: SearchDataDao,
    private val savedSearchDao: SavedSearchDao
) {

    private val authorization = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"

    suspend fun fetchApi() {
        withContext(Dispatchers.IO) {

            val retrofitService = Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitService::class.java)

            val categoryGroupCodes = listOf("PM9", "CE7")
            val x = "127.05897078335246"
            val y = "37.506051888130386"
            val radius = 20000
            val format = "json"

            clearTable()

            for (categoryGroupCode in categoryGroupCodes) {
                try {
                    val response = retrofitService.requestProducts(
                        authorization,
                        format,
                        categoryGroupCode,
                        x,
                        y,
                        radius
                    ).awaitResponse()

                    if (response.isSuccessful) {
                        val kakaoData = response.body()
                        kakaoData?.documents?.forEach { document ->
                            val placeName = document.placeName
                            val addressName = document.addressName
                            val categoryGroupName = document.categoryGroupName
                            val xCoordinate = document.x.toDouble()
                            val yCoordinate = document.y.toDouble()

                            searchDataDao.insertSearchData(
                                SearchData(
                                    placeName,
                                    addressName,
                                    categoryGroupName,
                                    xCoordinate,
                                    yCoordinate
                                )
                            )
                        }
                    } else {
                        Log.e(
                            "Retrofit",
                            "API 요청 실패, 응답 코드: ${response.code()}, 메시지: ${response.message()}"
                        )
                    }
                } catch (e: Exception) {
                    Log.e("Retrofit", "API 요청 실패, 네트워크 에러: ${e.message}")
                }
            }
        }
    }

    suspend fun getAllSavedWords(): List<String> {
        return savedSearchDao.getAllSavedSearch().map { it.savedName }
    }


    suspend fun loadDb(): List<SearchData> {
        return searchDataDao.getAllSearchData()
    }

    suspend fun deleteSavedWord(savedWord: String) {
        val savedSearch = SavedSearch(savedWord)
        savedSearchDao.deleteSavedSearch(savedSearch)
    }

    suspend fun clearTable() {
        return searchDataDao.deleteAllSearchData()
    }


    suspend fun savePlaceName(name: String) {
        val savedSearch = SavedSearch(name)
        savedSearchDao.insertSavedSearch(savedSearch)
    }
}