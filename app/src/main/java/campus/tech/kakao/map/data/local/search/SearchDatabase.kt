package campus.tech.kakao.map.data.local.search

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SearchData::class, SavedSearch::class], version = 1)
abstract class SearchDatabase : RoomDatabase() {
    abstract fun searchDataDao(): SearchDataDao
    abstract fun savedSearchDao(): SavedSearchDao
}