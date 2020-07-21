package practice.moviio.data.repository

import practice.moviio.data.response.details.MovieDetails
import retrofit2.Response

interface DetailsRepository {

    suspend fun getDetails(id: String) : Response<MovieDetails>

    suspend fun insertMovie(movie: MovieDetails)

    suspend fun deleteMovie(movie: MovieDetails)
}