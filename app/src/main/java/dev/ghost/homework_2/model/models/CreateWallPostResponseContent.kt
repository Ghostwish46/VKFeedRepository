package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class CreateWallPostResponseContent(
    @SerializedName("post_id")
    val postId: Int
)