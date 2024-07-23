package campus.tech.kakao.map.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import campus.tech.kakao.map.domain.model.SavedSearch
import campus.tech.kakao.map.domain.model.SearchData

@Database(entities = [SearchData::class, SavedSearch::class], version = 1)
abstract class SearchDatabase : RoomDatabase() {
    abstract fun searchDataDAO(): SearchDataDAO
    abstract fun savedSearchDao(): SavedSearchDao

    companion object {
        @Volatile
        private var instance: SearchDatabase? = null

        fun getInstance(context: Context): SearchDatabase {
            return instance ?: synchronized(this) {
                val tempInstance = Room.databaseBuilder(
                    context.applicationContext,
                    SearchDatabase::class.java,
                    "search_db"
                ).build()
                instance = tempInstance
                tempInstance
            }
        }

        fun destroyInstance() {
            instance = null
        }
    }
}