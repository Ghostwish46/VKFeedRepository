package dev.ghost.homework_2.presentation.adapters

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.ghost.homework_2.presentation.post.PostsViewModel

class PostsViewModelFactory(
    val application: Application,
    private val isFavorite: Boolean
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PostsViewModel(application, isFavorite) as T
    }
}