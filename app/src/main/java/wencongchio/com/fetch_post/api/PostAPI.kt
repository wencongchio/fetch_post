package wencongchio.com.fetch_post.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import wencongchio.com.fetch_post.models.Comment
import wencongchio.com.fetch_post.models.CommentResponse
import wencongchio.com.fetch_post.models.PostResponse

interface PostAPI {
    @GET("posts")
    suspend fun getPost(): Response<PostResponse>

    @GET("comments")
    suspend fun getComment(@Query("postId") postId: Int): Response<CommentResponse>
}