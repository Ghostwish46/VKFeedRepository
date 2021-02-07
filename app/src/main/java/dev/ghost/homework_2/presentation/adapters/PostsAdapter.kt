package dev.ghost.homework_2.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import dev.ghost.homework_2.R
import dev.ghost.homework_2.model.entities.CommentWithOwner
import dev.ghost.homework_2.model.models.ItemType
import dev.ghost.homework_2.model.entities.PostWithOwner
import dev.ghost.homework_2.model.entities.Profile
import dev.ghost.homework_2.model.models.AdapterItem
import dev.ghost.homework_2.presentation.viewholders.CommentViewHolder
import dev.ghost.homework_2.presentation.viewholders.PostViewHolder
import dev.ghost.homework_2.presentation.viewholders.PostWithImageViewHolder
import dev.ghost.homework_2.presentation.viewholders.ProfileViewHolder

class PostsAdapter(
    val onPostLiked: (postWithOwner: PostWithOwner) -> Unit,
    val onPostRemoved: (postWithOwner: PostWithOwner) -> Unit,
    private val onPostClicked: (postWithOwner: PostWithOwner) -> Unit,
    private val onImageClicked: (postWithOwner: PostWithOwner) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), PostTouchHelperAdapter {

    private val differ = AsyncListDiffer(this, PostsDiffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ItemType.POST_WITH_IMAGE.ordinal -> {
                val view = layoutInflater.inflate(R.layout.item_post_with_image, parent, false)
                PostWithImageViewHolder(view)
            }
            ItemType.POST_WITHOUT_IMAGE.ordinal -> {
                val view = layoutInflater.inflate(R.layout.item_post_without_image, parent, false)
                PostViewHolder(view)
            }
            ItemType.PROFILE.ordinal -> {
                val view = layoutInflater.inflate(R.layout.item_profile, parent, false)
                ProfileViewHolder(view)
            }
            else -> {
                val view = layoutInflater.inflate(R.layout.item_comment, parent, false)
                CommentViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun getItem(position: Int): AdapterItem = differ.currentList[position]

    override fun getItemViewType(position: Int): Int {
        val currentItem = differ.currentList[position]
        return currentItem.getType().ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        when (holder) {
            is PostWithImageViewHolder -> {
                if (currentItem is PostWithOwner)
                    holder.bind(currentItem, onPostClicked, onPostLiked, onImageClicked)
            }
            is PostViewHolder -> {
                if (currentItem is PostWithOwner)
                    holder.bind(currentItem, onPostClicked, onPostLiked)
            }
            is ProfileViewHolder -> {
                if (currentItem is Profile)
                    holder.bind(currentItem)
            }
            is CommentViewHolder -> {
                if (currentItem is CommentWithOwner)
                    holder.bind(currentItem)
            }
        }

    }

    fun submitList(newItems: List<AdapterItem>) {
        differ.submitList(newItems)
    }

    override fun onItemSwipedRight(position: Int) {
        val currentItem = differ.currentList[position]
        if (currentItem is PostWithOwner) {
            onPostLiked(currentItem)
            notifyItemChanged(position)
        }
    }

    override fun onItemSwipedLeft(position: Int) {
        val currentItem = differ.currentList[position]
        if (currentItem is PostWithOwner) {
            onPostRemoved(currentItem)
            notifyItemChanged(position)
        }
    }
}