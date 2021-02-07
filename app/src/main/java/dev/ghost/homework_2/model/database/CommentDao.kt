package dev.ghost.homework_2.model.database

import androidx.room.*
import dev.ghost.homework_2.model.entities.Comment
import dev.ghost.homework_2.model.entities.CommentWithOwner
import io.reactivex.Flowable

@Dao
interface CommentDao {
    @Query("Select * from comments")
    @Transaction
    fun getAllComments(): Flowable<List<Comment>>

    @Query("Select * from comments where postId = :postId")
    @Transaction
    fun getCommentsByPost(postId: Int): Flowable<List<CommentWithOwner>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(comment: Comment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(comments: List<Comment>)

    @Delete
    fun delete(comment: Comment)
}