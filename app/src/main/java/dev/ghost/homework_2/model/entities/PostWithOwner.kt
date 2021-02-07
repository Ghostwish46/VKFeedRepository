package dev.ghost.homework_2.model.entities

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import dev.ghost.homework_2.model.models.AdapterItem
import dev.ghost.homework_2.model.models.ItemType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostWithOwner(
    @Embedded
    val post: Post,

    @Relation(
        parentColumn = "sourceId",
        entityColumn = "id",
        entity = Group::class
    )
    val group: Group?,

    @Relation(
        parentColumn = "profileId",
        entityColumn = "id",
        entity = Profile::class
    )
    val profile: Profile?,

    @Relation(
        parentColumn = "id",
        entityColumn = "postId",
        entity = Photo::class
    )
    val postPhotos: List<Photo>

) : Parcelable, AdapterItem {
    override fun getType(): ItemType {
        return if (postPhotos.isEmpty())
            ItemType.POST_WITHOUT_IMAGE
        else
            ItemType.POST_WITH_IMAGE
    }

    fun getOwnerName(): String {
        return group?.name ?: (profile?.getFullName() ?: "")
    }

    fun getOwnerId(): Int {
        return group?.id ?: (profile?.id ?: 0)
    }

    fun getOwnerPhoto(): String {
        return group?.photo50 ?: (profile?.photo50 ?: "")
    }
}