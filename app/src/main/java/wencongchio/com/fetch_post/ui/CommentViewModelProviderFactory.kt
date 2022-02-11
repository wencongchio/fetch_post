package wencongchio.com.fetch_post.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import wencongchio.com.fetch_post.repository.CommentRepository

class CommentViewModelProviderFactory(val app: Application, val commentRepository: CommentRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommentViewModel(app, commentRepository) as T
    }
}