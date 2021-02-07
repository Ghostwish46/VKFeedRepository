package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class WallResponseContent(
    @SerializedName("items")
    val wallItems: List<Wall> = listOf(),
    @SerializedName("groups")
    val groups: List<Group> = listOf(),
    @SerializedName("profiles")
    val profiles: List<UserResponseContent> = listOf(),
)