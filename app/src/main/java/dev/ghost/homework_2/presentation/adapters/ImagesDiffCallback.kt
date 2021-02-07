package dev.ghost.homework_2.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import dev.ghost.homework_2.model.entities.Photo

object ImagesDiffCallback : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
    }
}