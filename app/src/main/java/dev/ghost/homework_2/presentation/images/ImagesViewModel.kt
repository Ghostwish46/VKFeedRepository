package dev.ghost.homework_2.presentation.images

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dev.ghost.homework_2.model.database.AppDatabase
import dev.ghost.homework_2.model.repositories.PhotoRepository
import dev.ghost.homework_2.App
import dev.ghost.homework_2.presentation.adapters.ImagesAdapter
import javax.inject.Inject

class ImagesViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var appDatabase: AppDatabase
    @Inject
    lateinit var photoRepository: PhotoRepository

    private val imagesAdapter = ImagesAdapter()
    private var postId: Int = 0
    private lateinit var imagesPresenter: ImagesPresenter

    init {
        (application as App).appComponent.inject(this)
    }

    fun setPostId(postId: Int) {
        this.postId = postId
        imagesPresenter =
            ImagesPresenter(
                photoRepository,
                postId,
                getApplication()
            )
    }

    fun getPresenter() = imagesPresenter
    fun getImagesAdapter() = imagesAdapter

}