package dev.ghost.homework_2.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.ghost.homework_2.model.entities.Comment
import dev.ghost.homework_2.model.entities.Photo
import dev.ghost.homework_2.model.entities.Post
import dev.ghost.homework_2.model.entities.Profile

@Database(
    entities = [Post::class, dev.ghost.homework_2.model.entities.Group::class, Photo::class, Profile::class, Comment::class],
    version = 3, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val postDao: PostDao
    abstract val groupDao: GroupDao
    abstract val photoDao: PhotoDao
    abstract val profileDao: ProfileDao
    abstract val commentDao: CommentDao
}