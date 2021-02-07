package dev.ghost.homework_2.model.database

import androidx.room.*
import dev.ghost.homework_2.model.entities.Post
import dev.ghost.homework_2.model.entities.PostWithOwner
import io.reactivex.Flowable

@Dao
interface PostDao {
    @Query("Select * from post order by date desc")
    @Transaction
    fun getAllPosts(): Flowable<List<PostWithOwner>>

    @Query("Select * from post where isOnOwnWall = 1 order by date desc")
    @Transaction
    fun getAllWallPosts(): Flowable<List<PostWithOwner>>

    @Query("Select * from post where date > :date order by date desc")
    @Transaction
    fun getPostsAfterDate(date: Long): Flowable<List<PostWithOwner>>

    @Query("Select * from post where isLiked = 1 and isOnOwnWall = 0 order by date desc")
    @Transaction
    fun getFavouritePosts(): Flowable<List<PostWithOwner>>

    @Query("Select * from post where id = :postId")
    @Transaction
    fun getPostById(postId: Int): Flowable<List<PostWithOwner>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(posts: List<Post>)

    @Delete
    fun delete(post: Post)

    @Query("Update post SET isLiked = :isLiked, likes = :likesCount where id = :postId")
    fun like(postId: Int, isLiked: Boolean, likesCount: Int)

    @Query("Update post SET isLiked = :isLiked where id = :postId")
    fun like(postId: Int, isLiked: Boolean)

    @Update
    fun update(post: Post)
}