package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class CommentResponse(
    @SerializedName("response")
    val commentResponseContent: CommentResponseContent,
)