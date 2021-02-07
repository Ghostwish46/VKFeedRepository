package dev.ghost.homework_2.model.repositories

import dev.ghost.homework_2.model.database.PhotoDao
import dev.ghost.homework_2.model.entities.Photo
import dev.ghost.homework_2.model.models.Attachment

class PhotoRepository(
    private val photoDao: PhotoDao
) {
    fun getPhotosByPost(postId: Int) = photoDao.getAllByPost(postId)

    fun add(photos: List<Photo>) {
        photoDao.add(photos)
    }

    fun add(photo: Photo) {
        photoDao.add(photo)
    }

    fun updateReceivedPhotos(attachments: List<Attachment>, postId: Int) {
        attachments.filter {
            it.type == "photo"
        }.forEach { attachment ->
            if (attachment.photo.sizes.isNotEmpty()) {
                val maxPhoto = attachment.photo.sizes.maxByOrNull { it.getSquare() }
                maxPhoto?.let {
                    add(
                        Photo(
                            id = attachment.photo.id,
                            postId = postId,
                            url = it.url,
                            height = it.height,
                            width = it.width
                        )
                    )
                }
            }
        }
    }
}