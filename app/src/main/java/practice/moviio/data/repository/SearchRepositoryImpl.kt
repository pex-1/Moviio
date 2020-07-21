package practice.moviio.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import practice.moviio.BuildConfig
import practice.moviio.api.OmdbApi
import practice.moviio.data.database.MovieDao
import practice.moviio.data.response.details.MovieDetails
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(private val movieDao: MovieDao, private val apiService: OmdbApi) : SearchRepository {

    override suspend fun insertMovie(movie: MovieDetails) {
        CoroutineScope(IO).launch {
            movieDao.insertMovie(movie)
        }
    }

    override suspend fun deleteMovie(id: String) {
        CoroutineScope(IO).launch {
            movieDao.deleteMovieUsingId(id)
        }
    }

    override suspend fun getMovies(query: String) = apiService.getMovies(BuildConfig.OmdbApiKey, query)

    override suspend fun getDetails(id: String) = apiService.getMovieDetails(BuildConfig.OmdbApiKey, id)


}