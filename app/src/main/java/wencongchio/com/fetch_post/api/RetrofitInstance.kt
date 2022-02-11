package wencongchio.com.fetch_post.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import wencongchio.com.fetch_post.util.Constant.Companion.BASE_URL

class RetrofitInstance {
    companion object{

        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val postAPI by lazy{
            retrofit.create(PostAPI::class.java)
        }

        val commentAPI by lazy{
            retrofit.create(CommentAPI::class.java)
        }
    }
}