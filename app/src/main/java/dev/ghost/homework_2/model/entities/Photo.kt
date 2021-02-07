package dev.ghost.homework_2.model.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.ghost.homework_2.model.models.AdapterItem
import dev.ghost.homework_2.model.models.ItemType
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey
    val id: Int,
    val url: String,
    val postId: Int,
    val height: Int,
    val width: Int
) : Parcelable, AdapterItem {
    override fun getType(): ItemType {
        return ItemType.IMAGE
    }
}