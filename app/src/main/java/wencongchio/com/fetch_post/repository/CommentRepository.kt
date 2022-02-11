package wencongchio.com.fetch_post.repository

import wencongchio.com.fetch_post.api.RetrofitInstance

class CommentRepository {
    suspend fun getComment(postId: Int) = RetrofitInstance.commentAPI.getComment(postId)
}