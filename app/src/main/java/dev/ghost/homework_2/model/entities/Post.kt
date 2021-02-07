package dev.ghost.homework_2.model.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.sql.Date
import java.text.SimpleDateFormat

@Parcelize
@Entity(tableName = "post")
data class Post(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val date: Long = 0,
    val sourceId: Int? = null,
    val profileId: Int? = null,
    val text: String = "",
    var likes: Int = 0,
    val comments: Int = 0,
    val views: Int = 0,
    var isLiked: Boolean = false,
    var isHidden: Boolean = false,
    var isOnOwnWall: Boolean = false,
    val reposts: Int,
    val canComment: Boolean
) : Parcelable {
    @Ignore
    val longFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm")

    fun getFullStringDate(): String = longFormatter.format(Date(date * 1000))
}