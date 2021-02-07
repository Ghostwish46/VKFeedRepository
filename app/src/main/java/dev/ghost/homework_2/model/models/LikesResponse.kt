package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class LikesResponse(
    @SerializedName("likes")
    val likes: Int
)