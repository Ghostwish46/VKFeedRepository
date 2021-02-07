package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class WallResponse(
    @SerializedName("response")
    val wallResponseContent: WallResponseContent
)