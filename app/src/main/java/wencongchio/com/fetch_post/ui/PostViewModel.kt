package wencongchio.com.fetch_post.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import wencongchio.com.fetch_post.PostApplication
import wencongchio.com.fetch_post.models.PostResponse
import wencongchio.com.fetch_post.repository.PostRepository
import wencongchio.com.fetch_post.util.Resource

class PostViewModel(app: Application, val postRepository: PostRepository): AndroidViewModel(app) {

    val postLiveData: MutableLiveData<Resource<PostResponse>> = MutableLiveData()

    init{
        getPost()
    }

    fun getPost() = viewModelScope.launch {
        postLiveData.postValue(Resource.Loading())
        try {
            if (checkInternetConnection()){
                val response = postRepository.getPost()
                postLiveData.postValue(handlePostResponse(response))
            } else {
                postLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> postLiveData.postValue(Resource.Error("Network Failure"))
                else -> postLiveData.postValue(Resource.Error("Conversion Error"))
            }

        }
    }

    private fun handlePostResponse(response: Response<PostResponse>): Resource<PostResponse>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun checkInternetConnection(): Boolean {
        val connectivityManager = getApplication<PostApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}