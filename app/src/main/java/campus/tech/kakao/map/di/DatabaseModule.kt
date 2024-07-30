package campus.tech.kakao.map.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import campus.tech.kakao.map.data.local.search.SavedSearchDao
import campus.tech.kakao.map.data.local.search.SearchDataDao
import campus.tech.kakao.map.data.local.search.SearchDatabase
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

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("KakaoMapReadyData", Context.MODE_PRIVATE)
    }

}