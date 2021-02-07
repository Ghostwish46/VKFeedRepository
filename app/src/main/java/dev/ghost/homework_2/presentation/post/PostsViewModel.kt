package dev.ghost.homework_2.presentation.post

import android.app.Application
import androidx.lifecycle.*
import dev.ghost.homework_2.model.entities.PostWithOwner
import dev.ghost.homework_2.model.repositories.GroupRepository
import dev.ghost.homework_2.model.repositories.PhotoRepository
import dev.ghost.homework_2.model.repositories.PostsRepository
import dev.ghost.homework_2.presentation.adapters.PostsAdapter
import dev.ghost.homework_2.App
import javax.inject.Inject

class PostsViewModel(application: Application, isFavorite: Boolean) :
    AndroidViewModel(application) {
    @Inject
    lateinit var postsRepository: PostsRepository

    @Inject
    lateinit var groupsRepository: GroupRepository

    @Inject
    lateinit var photosRepository: PhotoRepository

    private var postsPresenter: PostsPresenter

    private val postsAdapter: PostsAdapter

    init {
        (application as App).appComponent.inject(this)

        postsPresenter =
            PostsPresenter(postsRepository, groupsRepository, photosRepository, isFavorite)

        postsAdapter = PostsAdapter({
            onPostLiked(it)
        }, {
            onPostRemoved(it)
        }, {
            onPostClicked(it)
        }, {
            onImageClicked(it)
        })
    }

    private fun onPostLiked(postWithOwner: PostWithOwner) {
        val isLiked = !postWithOwner.post.isLiked
        postsPresenter.likePost(postWithOwner.post.id, postWithOwner.getOwnerId(), isLiked)
    }

    private fun onPostRemoved(postWithOwner: PostWithOwner) {
        postsPresenter.hidePost(postWithOwner)
    }

    private fun onPostClicked(postWithOwner: PostWithOwner) {
        postsPresenter.showPostDetails(postWithOwner)
    }

    private fun onImageClicked(postWithOwner: PostWithOwner) {
        postsPresenter.showPostImages(postWithOwner)
    }

    fun getPostsPresenter() = postsPresenter
    fun getPostsAdapter() = postsAdapter
}