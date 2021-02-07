package dev.ghost.homework_2.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import dev.ghost.homework_2.model.entities.CommentWithOwner
import dev.ghost.homework_2.model.entities.PostWithOwner
import dev.ghost.homework_2.model.entities.Profile
import dev.ghost.homework_2.model.models.AdapterItem

object PostsDiffCallback : DiffUtil.ItemCallback<AdapterItem>() {
    override fun areItemsTheSame(oldItem: AdapterItem, newItem: AdapterItem): Boolean {
        return if (oldItem is PostWithOwner && newItem is PostWithOwner)
            oldItem.post.id == newItem.post.id
        else if (oldItem is Profile && newItem is Profile)
            oldItem.id == newItem.id
        else if (oldItem is CommentWithOwner && newItem is CommentWithOwner)
            oldItem.comment.id == newItem.comment.id
        else
            false
    }

    override fun areContentsTheSame(oldItem: AdapterItem, newItem: AdapterItem): Boolean {
        return oldItem.equals(newItem)
    }
}