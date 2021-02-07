package dev.ghost.homework_2.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dev.ghost.homework_2.model.repositories.PostsRepository
import dev.ghost.homework_2.App
import javax.inject.Inject

class MenuViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var postsRepository: PostsRepository

    init {
        (application as App).appComponent.inject(this)
    }

    private val favouritePosts = postsRepository.getFavouritePosts()

    fun getFavouritePosts() = favouritePosts
}