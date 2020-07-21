package practice.moviio.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import practice.moviio.data.repository.FavoritesRepositoryImpl
import practice.moviio.data.response.details.MovieDetails
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(private val repository: FavoritesRepositoryImpl) : ViewModel() {

    val favorites: LiveData<ArrayList<MovieDetails>>

    init {
        getFavorites()
        favorites = repository.favoritesResponse()
    }

    fun getFavorites() = viewModelScope.launch(Dispatchers.Main) {
        repository.getFavorites()
    }

    fun deleteMovie(movie: MovieDetails) = viewModelScope.launch(Dispatchers.Main) {
        repository.deleteMovie(movie)
    }

}