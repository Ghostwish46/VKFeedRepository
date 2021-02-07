package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class Size(
    @SerializedName("height")
    val height: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("width")
    val width: Int
) {
    fun getSquare() = height * width
}