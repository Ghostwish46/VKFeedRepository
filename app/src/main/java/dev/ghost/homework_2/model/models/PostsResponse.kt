package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class PostsResponse(
    @SerializedName("response")
    val response: ResponseContent
)