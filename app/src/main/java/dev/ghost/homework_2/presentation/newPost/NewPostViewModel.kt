package dev.ghost.homework_2.presentation.newPost

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dev.ghost.homework_2.model.repositories.PostsRepository
import dev.ghost.homework_2.App
import javax.inject.Inject

class NewPostViewModel(application: Application) : AndroidViewModel(application) {
    @Inject
    lateinit var postsRepository: PostsRepository

    private var newPostPresenter: NewPostPresenter

    init {
        (application as App).appComponent.inject(this)
        newPostPresenter = NewPostPresenter(postsRepository)
    }

    fun sendPost(message: String) = getPresenter().sendPost(message)

    fun getPresenter() = newPostPresenter
}