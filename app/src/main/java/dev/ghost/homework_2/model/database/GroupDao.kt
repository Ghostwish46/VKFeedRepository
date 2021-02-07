package dev.ghost.homework_2.model.database

import androidx.room.*
import dev.ghost.homework_2.model.entities.Group
import io.reactivex.Flowable

@Dao
interface GroupDao {
    @Query("Select * from `group`")
    @Transaction
    fun getAllGroups(): Flowable<List<Group>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(group: Group)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(groups: List<Group>)

    @Delete
    fun delete(group: Group)
}