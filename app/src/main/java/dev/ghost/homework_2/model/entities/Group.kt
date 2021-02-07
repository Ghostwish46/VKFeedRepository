package dev.ghost.homework_2.model.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "group")
data class Group(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val name: String = "",
    val photo50: String = ""
) : Parcelable