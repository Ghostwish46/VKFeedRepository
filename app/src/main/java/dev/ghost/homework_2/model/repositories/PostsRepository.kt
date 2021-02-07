package dev.ghost.homework_2.model.repositories

import dev.ghost.homework_2.model.database.PostDao
import dev.ghost.homework_2.model.entities.Post
import dev.ghost.homework_2.model.models.Item
import dev.ghost.homework_2.model.models.LikePostResponse
import dev.ghost.homework_2.model.models.Wall
import dev.ghost.homework_2.model.network.ApiService
import io.reactivex.Single

import retrofit2.Response

class PostsRepository(
    private val postDao: PostDao,
    private val apiService: ApiService
) {
    private val favPosts = postDao.getFavouritePosts()

    fun getPostsAfterDate(date: Long) = postDao.getPostsAfterDate(date)
    fun getFavouritePosts() = favPosts
    fun getAllWallPosts() = postDao.getAllWallPosts()
    fun getPostById(postId: Int) = postDao.getPostById(postId)

    fun add(post: Post) {
        postDao.add(post)
    }

    fun likePostAsync(
        postId: Int,
        ownerId: Int,
        isLiked: Boolean
    ): Single<Response<LikePostResponse>> {
        return if (isLiked)
            apiService.likePost(postId, ownerId)
        else
            apiService.dislikePost(postId, ownerId)
    }

    fun hidePostAsync(postId: Int, ownerId: Int) =
        apiService.hidePost(postId, ownerId)

    fun updatePost(post: Post) {
        postDao.update(post)
    }

    fun changeLikes(postId: Int, isLiked: Boolean, likesCount: Int) {
        if (likesCount > -1)
            postDao.like(postId, isLiked, likesCount)
        else
            postDao.like(postId, isLiked)
    }

    fun deletePost(post: Post) {
        postDao.delete(post)
    }


    fun refresh() = apiService.getPostsAsync()
    fun refreshWall() = apiService.getWallAsync()

    fun refreshPostById(postData: String) = apiService.getPostByIdAsync(postData)

    fun sendWallPostAsync(message: String) = apiService.postWallAsync(message)

    fun updateReceivedPosts(posts: List<Item>) {
        posts.forEach { post ->
            add(
                Post(
                    id = post.postId,
                    date = post.date,
                    isHidden = false,
                    isLiked = post.like.userLikes != 0,
                    likes = post.like.count,
                    text = post.text,
                    views = if (post.view != null) post.view.count else 0,
                    sourceId = post.sourceId,
                    comments = post.commentProperty.count,
                    canComment = post.commentProperty.canPost == 1,
                    reposts = post.repost.count
                )
            )
        }
    }

    fun updateReceivedWall(posts: List<Wall>) {
        posts.forEach { post ->
            add(
                Post(
                    id = post.id,
                    date = post.date,
                    isHidden = false,
                    isLiked = post.like.userLikes != 0,
                    likes = post.like.count,
                    text = post.text,
                    views = if (post.view != null) post.view.count else 0,
                    profileId = post.fromId,
                    comments = post.commentProperty.count,
                    isOnOwnWall = true,
                    canComment = post.commentProperty.canPost == 1,
                    reposts = post.repost.count
                )
            )
        }
    }
}