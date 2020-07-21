package practice.moviio.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import practice.moviio.data.database.MovieDao
import practice.moviio.data.response.details.MovieDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(private val movieDao: MovieDao) : FavoritesRepository {

    private val favorites = MutableLiveData<ArrayList<MovieDetails>>()
    fun favoritesResponse(): LiveData<ArrayList<MovieDetails>> = favorites

    override suspend fun getFavorites() {
        withContext(IO) {
            val temp = movieDao.getFavorites()
            favorites.postValue(ArrayList(temp))
        }
    }

    override suspend fun deleteMovie(movie: MovieDetails) {
        CoroutineScope(IO).launch {
            movieDao.deleteMovie(movie)
        }
    }

}