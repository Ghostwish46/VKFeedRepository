package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class CommentProperty(
    @SerializedName("count")
    val count: Int,
    @SerializedName("can_post")
    val canPost: Int
)