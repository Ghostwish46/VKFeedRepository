package dev.ghost.homework_2.model.models

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class CommentModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @SerializedName("from_id")
    val fromId: Int,
    @SerializedName("post_id")
    val postId: Int,
    val date: Long,
    val text: String,
    val likes: Like,
)