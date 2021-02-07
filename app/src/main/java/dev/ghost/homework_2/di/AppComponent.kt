package dev.ghost.homework_2.di

import android.app.Application
import dagger.Component
import dev.ghost.homework_2.presentation.newPost.NewPostViewModel
import dev.ghost.homework_2.presentation.post.PostsFragment
import dev.ghost.homework_2.presentation.post.PostsViewModel
import dev.ghost.homework_2.presentation.profile.ProfileViewModel
import dev.ghost.homework_2.presentation.images.ImagesViewModel
import dev.ghost.homework_2.presentation.main.MenuViewModel
import dev.ghost.homework_2.presentation.postDetails.PostDetailsViewModel
import javax.inject.Singleton

@Component(modules = [AppModule::class, DataBaseModule::class, NetworkModule::class, StorageModule::class])
@Singleton
interface AppComponent {
    // Application
    fun inject(app: Application)

    // Activities

    // Fragments
    fun inject(fragment: PostsFragment)

    // ViewModels
    fun inject(viewModel: PostsViewModel)
    fun inject(viewModel: ImagesViewModel)
    fun inject(viewModel: MenuViewModel)
    fun inject(viewModel: PostDetailsViewModel)
    fun inject(viewModel: ProfileViewModel)
    fun inject(viewModel: NewPostViewModel)
}