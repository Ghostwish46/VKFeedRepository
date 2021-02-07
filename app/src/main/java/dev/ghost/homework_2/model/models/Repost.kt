package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class Repost(
    @SerializedName("count")
    val count: Int,
    @SerializedName("user_reposted")
    val userReposted: Int
)