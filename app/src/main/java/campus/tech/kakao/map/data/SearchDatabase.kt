package campus.tech.kakao.map.data

import androidx.room.Database
import androidx.room.RoomDatabase
import campus.tech.kakao.map.domain.model.SavedSearch
import campus.tech.kakao.map.domain.model.SearchData

@Database(entities = [SearchData::class, SavedSearch::class], version = 1)
abstract class SearchDatabase : RoomDatabase() {
    abstract fun searchDataDao(): SearchDataDao
    abstract fun savedSearchDao(): SavedSearchDao
}