package wencongchio.com.fetch_post.repository

import wencongchio.com.fetch_post.api.RetrofitInstance

class PostRepository {
    suspend fun getPost() = RetrofitInstance.postAPI.getPost()
}