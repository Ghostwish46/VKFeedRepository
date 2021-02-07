package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class UserResponseContent(
    val id: Int = 0,
    @SerializedName("first_name")
    val firstName: String = "",
    @SerializedName("last_name")
    val lastName: String = "",
    @SerializedName("photo_50")
    val photo50: String = "",
    val domain: String = "",
    @SerializedName("photo_max_orig")
    val photoMax: String = "",
    val about: String = "",
    val status: String = "",
    val bdate: String = "",
    val city: City,
    val country: Country,
    val career: List<Career>,
    @SerializedName("university_name")
    val universityName: String = "",
    @SerializedName("followers_count")
    val followersCount: Int = 0,
    @SerializedName("last_seen")
    val lastSeen: LastSeen
) {
}