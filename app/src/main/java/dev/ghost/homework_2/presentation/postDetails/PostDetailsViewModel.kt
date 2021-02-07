package dev.ghost.homework_2.presentation.postDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dev.ghost.homework_2.model.entities.PostWithOwner
import dev.ghost.homework_2.model.repositories.*
import dev.ghost.homework_2.presentation.adapters.PostsAdapter
import dev.ghost.homework_2.App
import javax.inject.Inject

class PostDetailsViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var postsRepository: PostsRepository

    @Inject
    lateinit var groupsRepository: GroupRepository

    @Inject
    lateinit var photosRepository: PhotoRepository

    @Inject
    lateinit var commentRepository: CommentRepository

    @Inject
    lateinit var profileRepository: ProfileRepository

    private val postsAdapter: PostsAdapter

    private var postId: Int = 0

    private lateinit var postDetailsPresenter: PostDetailsPresenter

    init {
        (application as App).appComponent.inject(this)

        postsAdapter = PostsAdapter({
            onPostLiked(it)
        }, {
            // No actions.
        }, {
            // No actions.
        }, {
            onImageClicked(it)
        })
    }

    fun setPostId(postId: Int) {
        this.postId = postId
        postDetailsPresenter =
            PostDetailsPresenter(
                postsRepository,
                groupsRepository,
                profileRepository,
                commentRepository,
                postId
            )
    }

    private fun onPostLiked(postWithOwner: PostWithOwner) {
        val isLiked = !postWithOwner.post.isLiked
        postDetailsPresenter.likePost(postWithOwner.post.id, postWithOwner.getOwnerId(), isLiked)
    }

    private fun onImageClicked(postWithOwner: PostWithOwner) {
        postDetailsPresenter.showPostImages(postWithOwner)
    }

    fun sendComment(message: String) {
        postDetailsPresenter.sendComment(message)
    }

    fun getPostDetailsPresenter() = postDetailsPresenter
    fun getPostsAdapter() = postsAdapter
}