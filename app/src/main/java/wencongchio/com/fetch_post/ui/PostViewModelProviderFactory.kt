package wencongchio.com.fetch_post.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import wencongchio.com.fetch_post.repository.PostRepository

class PostViewModelProviderFactory(val app: Application, val postRepository: PostRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PostViewModel(app, postRepository) as T
    }
}