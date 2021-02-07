package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class CommentResponseContent(
    @SerializedName("items")
    val items: List<CommentModel>,
    @SerializedName("profiles")
    val profiles: List<UserResponseContent> = listOf(),
    @SerializedName("groups")
    val groups: List<Group> = listOf(),
    val count: Int
)