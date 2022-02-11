package wencongchio.com.fetch_post.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import wencongchio.com.fetch_post.PostApplication
import wencongchio.com.fetch_post.models.Comment
import wencongchio.com.fetch_post.models.CommentResponse
import wencongchio.com.fetch_post.repository.CommentRepository
import wencongchio.com.fetch_post.util.Resource

class CommentViewModel(app: Application, val commentRepository: CommentRepository): AndroidViewModel(app) {

    val commentLiveData: MutableLiveData<Resource<CommentResponse>> = MutableLiveData()

    fun getComment(postId: Int) = viewModelScope.launch {
        commentLiveData.postValue(Resource.Loading())
        try {
            if (checkInternetConnection()){
                val response = commentRepository.getComment(postId)
                commentLiveData.postValue(handleCommentResponse(response))
            } else {
                commentLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> commentLiveData.postValue(Resource.Error("Network Failure"))
                else -> commentLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleCommentResponse(response: Response<CommentResponse>): Resource<CommentResponse>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getFilteredList(query: String): List<Comment>? {
        return commentLiveData.value?.data?.filter {
            it.body.contains(query)
        }
    }

    private fun checkInternetConnection(): Boolean {
        val connectivityManager = getApplication<PostApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}