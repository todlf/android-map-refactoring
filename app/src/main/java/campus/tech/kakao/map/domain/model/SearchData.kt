package campus.tech.kakao.map.domain.model

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
){
    companion object {
        const val TABLE_NAME = "searchTable"
        const val TABLE_COLUMN_NAME = "name"
        const val TABLE_COLUMN_ADDRESS = "address"
        const val TABLE_COLUMN_CATEGORY = "category"
        const val TABLE_COLUMN_XCOORDINATE = "xCoordinate"
        const val TABLE_COLUMN_YCOORDINATE = "yCoordinate"

        const val SAVED_SEARCH_TABLE_NAME = "savedSearchTable"
        const val SAVED_SEARCH_COLUMN_NAME = "savename"
    }
}


