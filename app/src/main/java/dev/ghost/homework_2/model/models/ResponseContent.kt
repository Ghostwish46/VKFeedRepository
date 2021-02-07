package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class ResponseContent(
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("groups")
    val groups: List<Group>
)