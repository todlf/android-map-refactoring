package campus.tech.kakao.map.data

import android.content.ContentValues
import android.content.Context
import android.util.Log
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.domain.model.SavedSearch
import campus.tech.kakao.map.domain.model.SearchData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class SearchRepository private constructor(private val db: SearchDatabase) {

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

                            db.searchDataDAO().insertSearchData(
                                SearchData(placeName, addressName, categoryGroupName, xCoordinate, yCoordinate)
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
        return db.savedSearchDao().getAllSavedSearch().map { it.savedName }
    }


    suspend fun loadDb(): List<SearchData> {
        return db.searchDataDAO().getAllSearchData()
    }

    suspend fun deleteSavedWord(savedWord: String) {
        val savedSearch = SavedSearch(savedWord)
        db.savedSearchDao().deleteSavedSearch(savedSearch)
    }

    suspend fun clearTable() {
        return db.searchDataDAO().deleteAllSearchData()
    }


    suspend fun savePlaceName(name: String) {
        val savedSearch = SavedSearch(name)
        db.savedSearchDao().insertSavedSearch(savedSearch)
    }


    companion object {
        @Volatile
        private var instance: SearchRepository? = null

        fun getInstance(context: Context): SearchRepository {
            return instance ?: synchronized(this) {
                val db = SearchDatabase.getInstance(context)
                val newInstance = SearchRepository(db)
                instance = newInstance
                newInstance
            }
        }
    }
}