package practice.moviio.data.repository

import practice.moviio.data.response.details.MovieDetails
import practice.moviio.data.response.movies.MovieList
import retrofit2.Response

interface SearchRepository {

    suspend fun insertMovie(movie: MovieDetails)

    suspend fun deleteMovie(id: String)

    suspend fun getMovies(query: String) : Response<MovieList>

    suspend fun getDetails(id: String) : Response<MovieDetails>
}