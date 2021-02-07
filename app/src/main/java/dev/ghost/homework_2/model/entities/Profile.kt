package dev.ghost.homework_2.model.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import dev.ghost.homework_2.model.models.AdapterItem
import dev.ghost.homework_2.model.models.ItemType
import kotlinx.android.parcel.Parcelize
import java.sql.Date
import java.text.SimpleDateFormat

@Parcelize
@Entity(tableName = "profile")
data class Profile(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val photo50: String = "",
    val domain: String = "",
    val status: String = "",
    val photoMax: String = "",
    val about: String = "",
    val bdate: String = "",
    val city: String = "",
    val country: String = "",
    val career: String = "",
    val education: String = "",
    val followersCount: Int = 0,
    val lastSeen: Long = 0
) : Parcelable, AdapterItem {
    @Ignore
    val longFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm")

    fun getFullName() = "$firstName $lastName"
    fun getHomeFullName() = "$city, $country"

    fun getLastSeenText(): String = longFormatter.format(Date(lastSeen * 1000))

    override fun getType(): ItemType {
        return ItemType.PROFILE
    }
}