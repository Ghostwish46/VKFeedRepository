package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class CreateWallPostResponse(
    @SerializedName("response")
    val createWallPostResponseContent: CreateWallPostResponseContent
)