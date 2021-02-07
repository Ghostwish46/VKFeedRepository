package dev.ghost.homework_2.model.repositories

import dev.ghost.homework_2.model.database.CommentDao
import dev.ghost.homework_2.model.entities.Comment
import dev.ghost.homework_2.model.models.CommentModel
import dev.ghost.homework_2.model.network.ApiService

class CommentRepository(
    private val commentDao: CommentDao,
    private val apiService: ApiService
) {

    fun getCommentsByPost(postId: Int) = commentDao.getCommentsByPost(postId)

    fun refresh(postId: Int, ownerId: Int) = apiService.getCommentsAsync(postId, ownerId)

    fun add(comment: Comment) {
        commentDao.add(comment)
    }

    fun delete(comment: Comment) {
        commentDao.delete(comment)
    }

    fun sendComment(postId: Int, ownerId: Int, message: String) =
        apiService.postCommentAsync(postId, ownerId, message)

    fun updateReceivedComments(comments: List<CommentModel>) {
        comments.forEach {
            val comment = Comment(
                id = it.id,
                postId = it.postId,
                date = it.date,
                text = it.text,
                likes = it.likes.count,
                isLiked = it.likes.userLikes == 1
            )
            if (it.fromId < 0)
                comment.sourceId = it.fromId
            else
                comment.profileId = it.fromId
            add(
                comment
            )
        }
    }
}