package dev.ghost.homework_2.presentation.adapters

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import dev.ghost.homework_2.presentation.viewholders.PostViewHolder
import dev.ghost.homework_2.presentation.viewholders.PostWithImageViewHolder

interface PostTouchHelperAdapter {
    fun onItemSwipedRight(position: Int)
    fun onItemSwipedLeft(position: Int)
}

class PostTouchHelperCallback(
    private val adapter: PostTouchHelperAdapter
) :
    ItemTouchHelper.SimpleCallback(UP or DOWN, START or END) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == START) {
            adapter.onItemSwipedLeft(viewHolder.adapterPosition)
        }
        if (direction == END) {
            adapter.onItemSwipedRight(viewHolder.adapterPosition)
        }
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = 0
        var swipeFlags = 0

        if (viewHolder is PostViewHolder || viewHolder is PostWithImageViewHolder)
            swipeFlags = START or END

        return Callback.makeMovementFlags(
            dragFlags,
            swipeFlags
        )
    }
}