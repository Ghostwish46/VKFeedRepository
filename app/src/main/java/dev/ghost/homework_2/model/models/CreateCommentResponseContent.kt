package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class CreateCommentResponseContent(
    @SerializedName("comment_id")
    val commentId: Int
)