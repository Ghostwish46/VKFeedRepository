package dev.ghost.homework_2.model.database

import androidx.room.*
import dev.ghost.homework_2.model.entities.Photo
import io.reactivex.Flowable

@Dao
interface PhotoDao {
    @Query("Select * from photos")
    @Transaction
    fun getAll(): Flowable<List<Photo>>

    @Query("Select * from photos where postId = :postId")
    @Transaction
    fun getAllByPost(postId: Int): Flowable<List<Photo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(photo: Photo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(photos: List<Photo>)

    @Delete
    fun delete(photo: Photo)
}