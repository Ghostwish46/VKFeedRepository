package dev.ghost.homework_2.model.entities

import androidx.room.Embedded
import androidx.room.Relation
import dev.ghost.homework_2.model.models.AdapterItem
import dev.ghost.homework_2.model.models.ItemType

data class CommentWithOwner(
    @Embedded
    val comment: Comment,

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

    ) : AdapterItem {

    fun getOwnerName(): String {
        return group?.name ?: (profile?.getFullName() ?: "")
    }

    fun getOwnerId(): Int {
        return group?.id ?: (profile?.id ?: 0)
    }

    fun getOwnerPhoto(): String {
        return group?.photo50 ?: (profile?.photo50 ?: "")
    }

    override fun getType(): ItemType {
        return ItemType.COMMENT
    }
}