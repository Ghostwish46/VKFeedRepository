package dev.ghost.homework_2.model.models

import com.google.gson.annotations.SerializedName

class Wall(
    @SerializedName("from_id")
    var fromId: Int,
    @SerializedName("id")
    var id: Int,
    @SerializedName("date")
    val date: Long,
    @SerializedName("text")
    val text: String = "",
    @SerializedName("attachments")
    val attachments: List<Attachment>?,
    @SerializedName("comments")
    val commentProperty: CommentProperty,
    @SerializedName("likes")
    val like: Like,
    @SerializedName("reposts")
    val repost: Repost,
    @SerializedName("views")
    val view: View?,
    @SerializedName("is_favorite")
    val isFavorite: Boolean
)