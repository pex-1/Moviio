package practice.moviio.data.response.movies


import com.google.gson.annotations.SerializedName

data class MovieList(
    @SerializedName("Response")
    val response: String,
    @SerializedName("Search")
    val search: List<Movie>,
    @SerializedName("totalResults")
    val totalResults: Int
)