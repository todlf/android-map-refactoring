package campus.tech.kakao.map.domain.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SearchDataDAO {
    @Insert
    suspend fun insertSearchData(searchData: SearchData)

    @Query("SELECT * FROM search_table")
    suspend fun getAllSearchData(): List<SearchData>

    @Query("DELETE FROM search_table")
    suspend fun deleteAllSearchData()
}