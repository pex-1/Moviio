package practice.moviio.ui.details

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import practice.moviio.data.repository.DetailsRepositoryImpl
import practice.moviio.data.response.details.MovieDetails
import practice.moviio.utils.Resource
import practice.moviio.utils.hasInternetConnection
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class DetailsViewModel @Inject constructor(private val repository: DetailsRepositoryImpl, private val application: Application) : ViewModel() {

    fun insertMovie(movie: MovieDetails) = viewModelScope.launch(Dispatchers.Main) {
        repository.insertMovie(movie)
    }

    fun deleteMovie(movie: MovieDetails) = viewModelScope.launch(Dispatchers.Main) {
        repository.deleteMovie(movie)
    }


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

}