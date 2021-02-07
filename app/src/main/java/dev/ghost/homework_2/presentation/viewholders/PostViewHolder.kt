package dev.ghost.homework_2.presentation.viewholders

import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import dev.ghost.homework_2.model.entities.PostWithOwner
import dev.ghost.homework_2.views.PostViewGroup
import kotlinx.android.synthetic.main.view_post.view.*

open class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(
        postWithOwner: PostWithOwner,
        onPostClicked: (postWithOwner: PostWithOwner) -> Unit,
        onPostLiked: (postWithOwner: PostWithOwner) -> Unit
    ) = with(itemView) {
        itemView.setOnClickListener {
            onPostClicked(postWithOwner)
        }
        likesPostView.setOnClickListener {
            onPostLiked(postWithOwner)
        }
        commentsPostView.setOnClickListener {
            onPostClicked(postWithOwner)
        }
        if (itemView is CardView) {
            val postView = itemView.children.first()
            if (postView is PostViewGroup)
                postView.bindPost(postWithOwner)
        }

    }
}