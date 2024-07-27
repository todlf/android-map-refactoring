package campus.tech.kakao.map.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import campus.tech.kakao.map.domain.model.SavedSearch

@Dao
interface SavedSearchDao {
    @Insert
    suspend fun insertSavedSearch(savedSearch: SavedSearch)

    @Query("SELECT * FROM saved_search_table")
    suspend fun getAllSavedSearch(): List<SavedSearch>

    @Delete
    suspend fun deleteSavedSearch(savedSearch: SavedSearch)
}