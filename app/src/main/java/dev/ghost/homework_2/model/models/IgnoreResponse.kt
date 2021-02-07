package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class IgnoreResponse(
    @SerializedName("response")
    val response: Int
)