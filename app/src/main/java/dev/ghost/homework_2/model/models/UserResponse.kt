package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class UserResponse(
    @SerializedName("response")
    val userResponseContent: List<UserResponseContent>
)