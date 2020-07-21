package practice.moviio.ui.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import practice.moviio.data.repository.SearchRepositoryImpl
import practice.moviio.data.response.details.MovieDetails
import practice.moviio.data.response.movies.MovieList
import practice.moviio.utils.Resource
import practice.moviio.utils.hasInternetConnection
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val repository: SearchRepositoryImpl, private val application: Application) : ViewModel() {


    val movies: MutableLiveData<Resource<MovieList>> = MutableLiveData()

    fun getMovies(query: String) = viewModelScope.launch {
        safeSearchMoviesCall(query)
    }

    private suspend fun safeSearchMoviesCall(query: String) {
        movies.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(application)) {
                val response = repository.getMovies(query)
                movies.postValue(handleMoviesResponse(response))
            } else {
                movies.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> movies.postValue(Resource.Error("Network Failure"))
                else -> movies.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleMoviesResponse(response: Response<MovieList>): Resource<MovieList> {
        if (response.isSuccessful) {
            response.body()
                    ?.let { resultResponse ->
                        return Resource.Success(resultResponse)
                    }
        }
        return Resource.Error(response.message())
    }


    //get movie details to store them in the database
    val details: MutableLiveData<Resource<MovieDetails>> = MutableLiveData()

    fun getDetails(id: String) = viewModelScope.launch(Dispatchers.Main) {
        safeDetailsCall(id)
    }


    private suspend fun safeDetailsCall(id: String) {
        details.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(application)) {
                val response = repository.getDetails(id)
                details.postValue(handleDetailsResponse(response))
            } else {
                details.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> details.postValue(Resource.Error("Network Failure"))
                else -> details.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleDetailsResponse(response: Response<MovieDetails>): Resource<MovieDetails> {
        if (response.isSuccessful) {
            response.body()
                    ?.let { resultResponse ->
                        return Resource.Success(resultResponse)
                    }
        }
        return Resource.Error(response.message())
    }


    //database operations
    fun deleteMovie(id: String) = viewModelScope.launch(Dispatchers.Main) {
        repository.deleteMovie(id)
    }

    fun insertMovie(movie: MovieDetails) = viewModelScope.launch(Dispatchers.Main) {
        repository.insertMovie(movie)
    }
}