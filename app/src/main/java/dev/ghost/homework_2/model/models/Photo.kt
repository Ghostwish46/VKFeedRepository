package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class Photo(
    @SerializedName("id")
    val id: Int,
    @SerializedName("sizes")
    val sizes: List<Size>
)