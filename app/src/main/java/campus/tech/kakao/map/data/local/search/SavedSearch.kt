package campus.tech.kakao.map.data.local.search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_search_table")
data class SavedSearch(
    @PrimaryKey
    val savedName: String
)