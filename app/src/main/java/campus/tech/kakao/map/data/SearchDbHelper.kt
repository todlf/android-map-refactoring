package campus.tech.kakao.map.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import campus.tech.kakao.map.domain.model.SearchData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class SearchDbHelper(context: Context) : SQLiteOpenHelper(context, "searchDb", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE ${SearchData.TABLE_NAME} (" +
                    "   ${SearchData.TABLE_COLUMN_NAME} varchar(255)," +
                    "   ${SearchData.TABLE_COLUMN_ADDRESS} varchar(255)," +
                    "   ${SearchData.TABLE_COLUMN_CATEGORY} varchar(255)," +
                    "   ${SearchData.TABLE_COLUMN_XCOORDINATE} double," +
                    "   ${SearchData.TABLE_COLUMN_YCOORDINATE} double" +
                    ");"
        )

        db?.execSQL(
            "CREATE TABLE ${SearchData.SAVED_SEARCH_TABLE_NAME} (" +
                    "   ${SearchData.SAVED_SEARCH_COLUMN_NAME} varchar(255)" +
                    ");"
        )

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${SearchData.TABLE_NAME}")
        db?.execSQL("DROP TABLE IF EXISTS ${SearchData.SAVED_SEARCH_TABLE_NAME}")
        onCreate(db)
    }






}