package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class Career(
    @SerializedName("group_id")
    val groupId: Int?,
    val company: String?,
)