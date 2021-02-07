package dev.ghost.homework_2.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import dev.ghost.homework_2.R
import dev.ghost.homework_2.model.entities.Photo
import dev.ghost.homework_2.presentation.viewholders.ImageViewHolder

class ImagesAdapter : RecyclerView.Adapter<ImageViewHolder>() {

    private val differ = AsyncListDiffer(this, ImagesDiffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)
    }

    fun submitList(newList: List<Photo>) {
        differ.submitList(newList)
    }

    fun getCurrentItem(position: Int): Photo {
        return differ.currentList[position]
    }
}