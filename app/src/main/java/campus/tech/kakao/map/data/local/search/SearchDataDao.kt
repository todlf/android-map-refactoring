package campus.tech.kakao.map.data.local.search

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import campus.tech.kakao.map.data.local.search.SearchData

@Dao
interface SearchDataDao {
    @Insert
    suspend fun insertSearchData(searchData: SearchData)

    @Query("SELECT * FROM search_table")
    suspend fun getAllSearchData(): List<SearchData>

    @Query("DELETE FROM search_table")
    suspend fun deleteAllSearchData()
}