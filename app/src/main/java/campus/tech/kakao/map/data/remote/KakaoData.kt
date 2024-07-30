package campus.tech.kakao.map.data.remote

import com.google.gson.annotations.SerializedName

data class KakaoData(
    @SerializedName("documents") val documents: List<Document>,
    val meta: Meta
)

data class Meta(
    @SerializedName("is_end") val isEnd: Boolean,
    @SerializedName("pageable_count") val pageableCount: Int,
    @SerializedName("same_name") val sameName: Any,
    @SerializedName("total_count") val totalCount: Int
)

data class Document(
    @SerializedName("place_name") val placeName: String,
    @SerializedName("distance") val distance: String,
    @SerializedName("place_url") val placeUrl: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("address_name") val addressName: String,
    @SerializedName("road_address_name") val roadAddressName: String,
    @SerializedName("id") val id: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("category_group_code") val categoryGroupCode: String,
    @SerializedName("category_group_name") val categoryGroupName: String,
    @SerializedName("x") val x: String,
    @SerializedName("y") val y: String
)
