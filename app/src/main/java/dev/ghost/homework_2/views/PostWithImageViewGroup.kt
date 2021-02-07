package dev.ghost.homework_2.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.view.*
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import dev.ghost.homework_2.R
import dev.ghost.homework_2.model.entities.PostWithOwner
import kotlinx.android.synthetic.main.view_post_with_image.view.*

class PostWithImageViewGroup @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : PostViewGroup(context, attributeSet, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_post_with_image, this, true)
    }

    override fun measureContent(
        startHeight: Int,
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ): Int {
        var height = startHeight
        height = super.measureContent(height, widthMeasureSpec, heightMeasureSpec)

        measureChildWithMargins(
            imagePostView,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            height
        )
        measureChildWithMargins(
            buttonPostMoreImages,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            height
        )

        height += imagePostView.measuredHeight + imagePostView.marginBottom
        return height
    }

    override fun layoutContent(currentLeft: Int, startTop: Int): Int {
        var currentTop: Int = super.layoutContent(currentLeft, startTop)

        imagePostView.layout(
            currentLeft,
            currentTop,
            measuredWidth,
            currentTop + imagePostView.measuredHeight
        )

        buttonPostMoreImages.layout(
            measuredWidth - buttonPostMoreImages.measuredWidth - buttonPostMoreImages.marginLeft,
            currentTop + buttonPostMoreImages.marginTop,
            measuredWidth - buttonPostMoreImages.marginLeft,
            currentTop + buttonPostMoreImages.measuredHeight + buttonPostMoreImages.marginBottom
        )
        currentTop += imagePostView.measuredHeight + imagePostView.marginBottom

        return currentTop
    }

    override fun bindPost(currentPost: PostWithOwner) {
        super.bindPost(currentPost)
        if (currentPost.postPhotos.isNotEmpty()) {
            val shimmerForLoading = Shimmer.ColorHighlightBuilder()
                .setBaseColor(ContextCompat.getColor(context, R.color.colorPlaceholder))
                .setBaseAlpha(1F)
                .setHighlightColor(ContextCompat.getColor(context, R.color.colorGray))
                .setHighlightAlpha(1F)
                .setDropoff(50F)
                .build()
            val shimmerDrawable = ShimmerDrawable().apply {
                setShimmer(shimmerForLoading)
            }

            buttonPostMoreImages.isVisible = currentPost.postPhotos.size > 1
            if (buttonPostMoreImages.isVisible) {
                buttonPostMoreImages.text = "+ ${currentPost.postPhotos.size - 1} more"
            }

            Glide.with(imagePostView)
                .load(currentPost.postPhotos.first().url)
                .placeholder(shimmerDrawable)
                .error(R.drawable.ic_error_loading)
                .into(imagePostView)
        }
    }
}