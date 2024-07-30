package campus.tech.kakao.map.data.local.search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_table")
data class SearchData(
    @PrimaryKey
    val name: String,
    val address: String,
    val category: String,
    val x: Double,
    val y: Double
)