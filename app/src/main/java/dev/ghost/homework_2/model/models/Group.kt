package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class Group(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("photo_50")
    val photo50: String
)