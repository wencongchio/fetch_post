package wencongchio.com.fetch_post.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_comment.*
import wencongchio.com.fetch_post.R
import wencongchio.com.fetch_post.adapters.CommentAdapter
import wencongchio.com.fetch_post.adapters.HeaderAdapter
import wencongchio.com.fetch_post.models.Post
import wencongchio.com.fetch_post.repository.CommentRepository
import wencongchio.com.fetch_post.util.Constant.Companion.COMMENT_SECTION_TITLE
import wencongchio.com.fetch_post.util.Resource

class CommentActivity : AppCompatActivity() {

    lateinit var viewModel: CommentViewModel
    lateinit var commentAdapter: CommentAdapter
    lateinit var headerAdapter: HeaderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        val post: Post? = this.intent.extras?.getSerializable("post") as Post?
        val postId: Int? = post?.id

        val repository = CommentRepository()
        val viewModelProviderFactory = CommentViewModelProviderFactory(application, repository)
        val parentLayout: View = findViewById(R.id.cl_comment_activity)

        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(CommentViewModel::class.java)
        initRecyclerView()

        if (post != null && postId != null){
            viewModel.getComment(postId)
            txt_post_title_in_comment.text = post.title
            txt_post_content_in_comment.text = post.body
        } else {
            startLoading()
            Toast.makeText(this, "Error: no postID found", Toast.LENGTH_LONG).show()
        }

        sv_comment_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null){
                    filterComment(newText);
                }
                return false;
            }

        })

        viewModel.commentLiveData.observe(this, Observer{response ->
            when(response){
                is Resource.Success -> {
                    stopLoading()
                    response.data?.let{commentList ->
                        commentAdapter.differ.submitList(commentList)
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
        comment_loading_progressbar.visibility = View.INVISIBLE
    }

    private fun startLoading(){
        comment_loading_progressbar.visibility = View.VISIBLE
    }

    private fun initRecyclerView(){
        headerAdapter = HeaderAdapter(COMMENT_SECTION_TITLE)
        commentAdapter = CommentAdapter()
        val concatAdapter = ConcatAdapter(headerAdapter, commentAdapter)
        rv_comment_list.apply {
            adapter = concatAdapter
            layoutManager = LinearLayoutManager(this@CommentActivity)
        }
    }

    private fun filterComment(query: String){
        val filteredList = viewModel.getFilteredList(query)
        commentAdapter.differ.submitList(filteredList)
    }
}