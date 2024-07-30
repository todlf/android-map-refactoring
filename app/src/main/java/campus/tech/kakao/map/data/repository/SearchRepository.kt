package campus.tech.kakao.map.data.repository

import android.util.Log
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.data.local.search.SearchDataDao
import campus.tech.kakao.map.data.local.search.SavedSearchDao
import campus.tech.kakao.map.data.local.search.SavedSearch
import campus.tech.kakao.map.data.remote.RetrofitService
import campus.tech.kakao.map.data.local.search.SearchData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(
    private val searchDataDao: SearchDataDao,
    private val savedSearchDao: SavedSearchDao,
    private val retrofitService: RetrofitService
) {

    private val authorization = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"

    suspend fun fetchApi() {
        withContext(Dispatchers.IO) {

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


    suspend fun loadAllSearchData(): List<SearchData> {
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