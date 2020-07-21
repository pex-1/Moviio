package practice.moviio.data.response.movies


import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("imdbID")
    val imdbID: String,
    @SerializedName("Poster")
    val poster: String,
    @SerializedName("Title")
    val title: String,
    @SerializedName("Type")
    val type: String,
    @SerializedName("Year")
    val year: Int

) {
    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Movie
        if (imdbID != other.imdbID) return false
        if (poster != other.poster) return false
        if (title != other.title) return false
        if (type != other.type) return false
        if (year != other.year) return false

        return true
    }
}