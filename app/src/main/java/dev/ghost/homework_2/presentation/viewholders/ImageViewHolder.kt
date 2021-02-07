package dev.ghost.homework_2.presentation.viewholders

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import dev.ghost.homework_2.R
import dev.ghost.homework_2.model.entities.Photo
import kotlinx.android.synthetic.main.item_image.view.*

open class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(
        postPhoto: Photo
    ) = with(itemView) {

        val shimmerForLoading = Shimmer.ColorHighlightBuilder()
            .setBaseColor(ContextCompat.getColor(context, R.color.colorPlaceholder))
            .setBaseAlpha(1F)
            .setHighlightColor(ContextCompat.getColor(context, R.color.colorGray))
            .setHighlightAlpha(1F)
            .setDropoff(50F)
            .setFixedWidth(postPhoto.width)
            .setFixedHeight(postPhoto.height)
            .build()
        val shimmerDrawable = ShimmerDrawable().apply {
            setShimmer(shimmerForLoading)
        }

        Glide.with(context)
            .load(postPhoto.url)
            .placeholder(shimmerDrawable)
            .error(R.drawable.ic_error_loading)
            .into(imageViewItemImage)
    }
}