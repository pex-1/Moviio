package practice.moviio.api

import practice.moviio.data.response.details.MovieDetails
import practice.moviio.data.response.movies.MovieList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {

    @GET(".")
    suspend fun getMovies(@Query("apikey") apiKey: String, @Query("s") title: String, @Query("type") type: String = "movie") : Response<MovieList>

    @GET(".")
    suspend fun getMovieDetails(@Query("apikey") apiKey: String, @Query("i") id: String, @Query("type") type: String = "movie") : Response<MovieDetails>

}