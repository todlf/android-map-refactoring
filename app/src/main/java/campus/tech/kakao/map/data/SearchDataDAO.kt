package campus.tech.kakao.map.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import campus.tech.kakao.map.domain.model.SearchData

@Dao
interface SearchDataDAO {
    @Insert
    suspend fun insertSearchData(searchData: SearchData)

    @Query("SELECT * FROM search_table")
    suspend fun getAllSearchData(): List<SearchData>

    @Query("DELETE FROM search_table")
    suspend fun deleteAllSearchData()
}