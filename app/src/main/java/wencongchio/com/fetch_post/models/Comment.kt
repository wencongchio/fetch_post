package wencongchio.com.fetch_post.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "comment"
)

data class Comment(
    @PrimaryKey(autoGenerate = true)
    val body: String,
    val email: String,
    val id: Int,
    val name: String,
    val postId: Int
)