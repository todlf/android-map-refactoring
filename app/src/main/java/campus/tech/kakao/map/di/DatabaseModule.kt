package campus.tech.kakao.map.di

import android.content.Context
import androidx.room.Room
import campus.tech.kakao.map.data.SavedSearchDao
import campus.tech.kakao.map.data.SearchDataDao
import campus.tech.kakao.map.data.SearchDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideSearchDatabase(@ApplicationContext context: Context): SearchDatabase {
        return Room.databaseBuilder(
            context,
            SearchDatabase::class.java,
            "search_db"
        ).build()
    }

    @Provides
    fun provideSearchDataDao(database: SearchDatabase): SearchDataDao {
        return database.searchDataDao()
    }

    @Provides
    fun provideSavedSearchDataDao(database: SearchDatabase): SavedSearchDao {
        return database.savedSearchDao()
    }

}