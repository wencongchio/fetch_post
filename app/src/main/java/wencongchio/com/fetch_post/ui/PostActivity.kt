package wencongchio.com.fetch_post.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_post.*
import wencongchio.com.fetch_post.R
import wencongchio.com.fetch_post.adapters.HeaderAdapter
import wencongchio.com.fetch_post.adapters.PostAdapter
import wencongchio.com.fetch_post.repository.PostRepository
import wencongchio.com.fetch_post.util.Constant.Companion.POST_SECTION_TITLE
import wencongchio.com.fetch_post.util.Resource

class PostActivity : AppCompatActivity() {

    lateinit var viewModel: PostViewModel
    lateinit var postAdapter: PostAdapter
    lateinit var headerAdapter: HeaderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val repository = PostRepository()
        val viewModelProviderFactory = PostViewModelProviderFactory(application, repository)

        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(PostViewModel::class.java)
        initRecyclerView()

        postAdapter.setOnItemClickListener {
            val bundle = Bundle().apply{
                putSerializable("post", it)
            }
            val intent = Intent(this, CommentActivity::class.java).apply{
                putExtras(bundle)
            }
            startActivity(intent)
        }

        viewModel.postLiveData.observe(this, Observer{ response ->
            when(response){
                is Resource.Success -> {
                    stopLoading()
                    response.data?.let{postList ->
                        postAdapter.differ.submitList(postList)
                    }
                }
                is Resource.Error -> {
                    stopLoading()
                    response.message?.let{errorMessage ->
                        Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    startLoading()
                }
            }
        })
    }

    private fun stopLoading() {
        loading_progressbar.visibility = View.INVISIBLE
    }

    private fun startLoading(){
        loading_progressbar.visibility = View.VISIBLE
    }

    private fun initRecyclerView(){
        headerAdapter = HeaderAdapter(POST_SECTION_TITLE)
        postAdapter = PostAdapter()
        val concatAdapter = ConcatAdapter(headerAdapter, postAdapter)
        rv_post_list.apply {
            adapter = concatAdapter
            layoutManager = LinearLayoutManager(this@PostActivity)
        }
    }

}