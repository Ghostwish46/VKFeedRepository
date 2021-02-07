package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class Like(
    @SerializedName("count")
    val count: Int,
    @SerializedName("user_likes")
    val userLikes: Int,
    @SerializedName("can_like")
    val canLike: Int
)