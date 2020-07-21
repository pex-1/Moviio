package practice.moviio.data.repository

import practice.moviio.data.response.details.MovieDetails

interface FavoritesRepository {

    suspend fun getFavorites()

    suspend fun deleteMovie(movie: MovieDetails)
}