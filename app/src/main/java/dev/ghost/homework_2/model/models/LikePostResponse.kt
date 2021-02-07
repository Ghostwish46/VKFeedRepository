package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class LikePostResponse(
    @SerializedName("response")
    val response: LikesResponse
)