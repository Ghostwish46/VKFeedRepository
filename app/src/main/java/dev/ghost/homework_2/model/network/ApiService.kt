package dev.ghost.homework_2.model.network

import dev.ghost.homework_2.model.models.*
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("newsfeed.get?filters=post")
    fun getPostsAsync(
    ): Single<Response<PostsResponse>>

    @GET("wall.getById?extended=1")
    fun getPostByIdAsync(
        @Query("posts") posts: String
    ): Single<Response<PostsResponse>>

    @POST("likes.add?type=post")
    fun likePost(
        @Query("item_id") postId: Int,
        @Query("owner_id") ownerId: Int
    ): Single<Response<LikePostResponse>>

    @POST("likes.delete?type=post")
    fun dislikePost(
        @Query("item_id") postId: Int,
        @Query("owner_id") ownerId: Int
    ): Single<Response<LikePostResponse>>

    @POST("newsfeed.ignoreItem?type=wall")
    fun hidePost(
        @Query("item_id") postId: Int,
        @Query("owner_id") ownerId: Int
    ): Single<Response<IgnoreResponse>>


    @GET("wall.get?extended=1")
    fun getWallAsync(
    ): Single<Response<WallResponse>>

    @GET("users.get?fields=photo_max_orig,photo_50,domain,city,country,about,bdate,career,education,followers_count,last_seen,status")
    fun getUserInfo(
    ): Single<Response<UserResponse>>

    @GET("wall.getComments?need_likes=1&extended=1&count=100")
    fun getCommentsAsync(
        @Query("post_id") postId: Int,
        @Query("owner_id") ownerId: Int
    ): Single<Response<CommentResponse>>

    @POST("wall.createComment")
    fun postCommentAsync(
        @Query("post_id") postId: Int,
        @Query("owner_id") ownerId: Int,
        @Query("message") message:String,
    ): Single<Response<CreateCommentResponse>>

    @POST("wall.post")
    fun postWallAsync(
        @Query("message") message:String,
    ): Single<Response<CreateWallPostResponse>>
}