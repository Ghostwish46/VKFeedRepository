package dev.ghost.homework_2.model.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.sql.Date
import java.text.SimpleDateFormat

@Entity(tableName = "comments")
class Comment(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    var sourceId: Int? = null,
    var profileId: Int? = null,
    val postId: Int,
    val date: Long,
    val text: String,
    val likes: Int = 0,
    val isLiked: Boolean = false
) {
    @Ignore
    val longFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm")

    fun getDateText() = longFormatter.format(Date(date * 1000))
}