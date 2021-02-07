package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class Attachment(
    @SerializedName("type")
    val type: String,
    @SerializedName("photo")
    val photo: Photo
)