package dev.ghost.homework_2.presentation.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dev.ghost.homework_2.model.entities.PostWithOwner
import dev.ghost.homework_2.model.repositories.GroupRepository
import dev.ghost.homework_2.model.repositories.PhotoRepository
import dev.ghost.homework_2.model.repositories.PostsRepository
import dev.ghost.homework_2.model.repositories.ProfileRepository
import dev.ghost.homework_2.presentation.adapters.PostsAdapter
import dev.ghost.homework_2.App
import javax.inject.Inject

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    @Inject
    lateinit var postsRepository: PostsRepository

    @Inject
    lateinit var groupsRepository: GroupRepository

    @Inject
    lateinit var photosRepository: PhotoRepository

    @Inject
    lateinit var profileRepository: ProfileRepository

    private var profilePresenter: ProfilePresenter

    private val postsAdapter: PostsAdapter

    init {
        (application as App).appComponent.inject(this)

        profilePresenter =
            ProfilePresenter(postsRepository, groupsRepository, photosRepository, profileRepository)

        postsAdapter = PostsAdapter({
            onPostLiked(it)
        }, {
            // Никаких действий на удаление.
        }, {
            onPostClicked(it)
        }, {
            onImageClicked(it)
        })
    }

    private fun onPostLiked(postWithOwner: PostWithOwner) {
        val isLiked = !postWithOwner.post.isLiked
        profilePresenter.likePost(postWithOwner.post.id, postWithOwner.getOwnerId(), isLiked)
    }

    private fun onPostClicked(postWithOwner: PostWithOwner) {
        profilePresenter.showPostDetails(postWithOwner)
    }

    private fun onImageClicked(postWithOwner: PostWithOwner) {
        profilePresenter.showPostImages(postWithOwner)
    }

    fun getProfilePresenter() = profilePresenter
    fun getPostsAdapter() = postsAdapter
}