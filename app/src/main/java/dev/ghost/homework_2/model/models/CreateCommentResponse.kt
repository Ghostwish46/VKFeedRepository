package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class CreateCommentResponse(
    @SerializedName("response")
    val createCommentResponseContent: CreateCommentResponseContent
)