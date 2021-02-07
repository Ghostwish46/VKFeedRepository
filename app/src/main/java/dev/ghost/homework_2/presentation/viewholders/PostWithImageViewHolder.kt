package dev.ghost.homework_2.presentation.viewholders

import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import dev.ghost.homework_2.model.entities.PostWithOwner
import dev.ghost.homework_2.views.PostWithImageViewGroup
import kotlinx.android.synthetic.main.view_post.view.*
import kotlinx.android.synthetic.main.view_post_with_image.view.*

class PostWithImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val currentWidth = itemView.context.display?.width
    fun bind(
        postWithOwner: PostWithOwner,
        onPostClicked: (postWithOwner: PostWithOwner) -> Unit,
        onPostLiked: (postWithOwner: PostWithOwner) -> Unit,
        onImageClicked: (postWithOwner: PostWithOwner) -> Unit
    ) = with(itemView) {
        imagePostView.setOnClickListener {
            onImageClicked(postWithOwner)
        }
        itemView.setOnClickListener {
            onPostClicked(postWithOwner)
        }
        likesPostView.setOnClickListener {
            onPostLiked(postWithOwner)
        }
        commentsPostView.setOnClickListener {
            onPostClicked(postWithOwner)
        }
        buttonPostMoreImages.setOnClickListener {
            onImageClicked(postWithOwner)
        }

        if (this is CardView) {
            val postView = children.first()
            if (postView is PostWithImageViewGroup) {
                postView.bindPost(postWithOwner)
                currentWidth?.let {
                    val params = imagePostView.layoutParams
                    params.height =
                        currentWidth *
                                postWithOwner.postPhotos.first().height /
                                postWithOwner.postPhotos.first().width
                    itemView.imagePostView.layoutParams = params
                }
            }
        }
    }
}