package dev.ghost.homework_2.presentation.viewholders

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dev.ghost.homework_2.R
import dev.ghost.homework_2.model.entities.CommentWithOwner
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(
        commentWithOwner: CommentWithOwner
    ) = with(itemView) {

        commentOwnerName.text = commentWithOwner.getOwnerName()
        commentText.text = commentWithOwner.comment.text
        commentDate.text = commentWithOwner.comment.getDateText()
        commentLikes.text = commentWithOwner.comment.likes.toString()

        if (commentWithOwner.comment.isLiked)
            commentLikes.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_liked_16
                ), null, null, null
            )
        else
            commentLikes.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_like_16
                ), null, null, null
            )

        Glide.with(commentOwnerAvatar)
            .load(commentWithOwner.getOwnerPhoto())
            .error(R.drawable.placeholder_avatar)
            .transform(CenterCrop(), RoundedCorners(100))
            .into(commentOwnerAvatar)
    }
}