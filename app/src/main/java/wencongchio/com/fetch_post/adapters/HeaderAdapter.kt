package wencongchio.com.fetch_post.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_header.view.*
import wencongchio.com.fetch_post.R

class HeaderAdapter(title: String): RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {

    val headerTitle = title

    inner class HeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderAdapter.HeaderViewHolder {
        return HeaderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_header,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HeaderAdapter.HeaderViewHolder, position: Int) {
        holder.itemView.apply{
            txt_header_title.text = headerTitle
        }
    }

    override fun getItemCount(): Int {
        return 1
    }
}