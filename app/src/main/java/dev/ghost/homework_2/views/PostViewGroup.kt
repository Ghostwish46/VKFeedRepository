package dev.ghost.homework_2.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.content.ContextCompat
import androidx.core.view.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dev.ghost.homework_2.R
import dev.ghost.homework_2.model.entities.PostWithOwner
import kotlinx.android.synthetic.main.view_post.view.*
import kotlin.math.max

open class PostViewGroup @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attributeSet, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_post, this, true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = MeasureSpec.getSize(widthMeasureSpec)
        var height = 0

        height = measureHeader(height, widthMeasureSpec, heightMeasureSpec)
        height = measureContent(height, widthMeasureSpec, heightMeasureSpec)
        height = measureFooter(height, widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(desiredWidth, View.resolveSize(height, heightMeasureSpec))
    }

    fun measureHeader(startHeight: Int, widthMeasureSpec: Int, heightMeasureSpec: Int): Int {
        var height = startHeight
        measureChildWithMargins(
            imageGroupView,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            height
        )

        measureChildWithMargins(
            textGroupView,
            widthMeasureSpec - imageGroupView.measuredWidth,
            0,
            heightMeasureSpec,
            height
        )

        measureChildWithMargins(
            textDateView,
            widthMeasureSpec - imageGroupView.measuredWidth,
            0,
            heightMeasureSpec,
            height + textGroupView.measuredHeight
        )

        height += max(
            imageGroupView.measuredHeight + imageGroupView.marginBottom +
                    imageGroupView.marginTop,
            textGroupView.measuredHeight + textDateView.measuredHeight +
                    textDateView.marginBottom + +textDateView.marginBottom + textGroupView.marginBottom

        )
        return height
    }

    open fun measureContent(startHeight: Int, widthMeasureSpec: Int, heightMeasureSpec: Int): Int {
        return if (textContentView.text.isNotEmpty()) {
            var height = startHeight
            measureChildWithMargins(
                textContentView,
                widthMeasureSpec,
                0,
                heightMeasureSpec,
                height
            )
            height += textContentView.measuredHeight + textContentView.marginTop + textContentView.marginBottom
            height
        } else startHeight
    }

    fun measureFooter(startHeight: Int, widthMeasureSpec: Int, heightMeasureSpec: Int): Int {
        var height = startHeight
        measureChildWithMargins(
            likesPostView,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            height
        )

        measureChildWithMargins(
            commentsPostView,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            height
        )

        measureChildWithMargins(
            sharesPostView,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            height
        )

        height += likesPostView.measuredHeight + likesPostView.marginBottom
        return height
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentLeft = l + paddingLeft
        var currentTop = t + paddingTop

        currentTop = layoutHeader(currentLeft, currentTop)
        currentTop = layoutContent(currentLeft, currentTop)
        layoutFooter(currentLeft, currentTop)
    }

    fun layoutHeader(currentLeft: Int, startTop: Int): Int {
        var currentTop = startTop
        imageGroupView.layout(
            currentLeft + imageGroupView.marginLeft,
            currentTop + imageGroupView.marginTop,
            imageGroupView.measuredWidth + imageGroupView.marginLeft,
            currentTop + imageGroupView.measuredHeight + imageGroupView.marginTop
        )
        textGroupView.layout(
            imageGroupView.measuredWidth + imageGroupView.marginLeft + imageGroupView.marginRight,
            currentTop + textGroupView.marginTop,
            measuredWidth - textGroupView.marginRight,
            currentTop + textGroupView.measuredHeight + textGroupView.marginTop
        )
        textDateView.layout(
            imageGroupView.measuredWidth + imageGroupView.marginLeft + imageGroupView.marginRight,
            currentTop + textGroupView.measuredHeight + textGroupView.marginBottom + textDateView.marginTop + textGroupView.marginTop,
            measuredWidth - textDateView.marginRight,
            currentTop + textGroupView.measuredHeight + textDateView.measuredHeight
                    + textGroupView.marginBottom + textDateView.marginTop + textGroupView.marginTop
        )

        currentTop += max(
            imageGroupView.measuredHeight + imageGroupView.marginBottom + imageGroupView.marginTop,
            textGroupView.measuredHeight + textDateView.measuredHeight
                    + textGroupView.marginBottom + textDateView.marginBottom + textGroupView.marginTop
        )
        return currentTop
    }

    open fun layoutContent(currentLeft: Int, startTop: Int): Int {
        return if (textContentView.text.isNotEmpty()) {
            var currentTop = startTop
            textContentView.layout(
                currentLeft + textContentView.marginLeft,
                currentTop,
                measuredWidth - textContentView.marginRight,
                currentTop + textContentView.measuredHeight
            )
            currentTop += textContentView.measuredHeight + textContentView.marginBottom
            currentTop
        } else
            return startTop
    }

    fun layoutFooter(currentLeft: Int, startTop: Int): Int {
        val currentTop = startTop
        var offset = likesPostView.marginLeft
        likesPostView.layout(
            currentLeft + offset,
            currentTop,
            offset + likesPostView.measuredWidth,
            currentTop + likesPostView.measuredHeight
        )
        offset += likesPostView.measuredWidth + likesPostView.marginLeft + commentsPostView.marginLeft

        if (commentsPostView.isVisible) {
            commentsPostView.layout(
                currentLeft + offset,
                currentTop,
                offset + commentsPostView.measuredWidth,
                currentTop + commentsPostView.measuredHeight
            )
            offset += commentsPostView.measuredWidth + commentsPostView.marginLeft + sharesPostView.marginLeft
        }

        sharesPostView.layout(
            currentLeft + offset,
            currentTop,
            offset + sharesPostView.measuredWidth,
            currentTop + sharesPostView.measuredHeight
        )
        offset += sharesPostView.measuredWidth + sharesPostView.marginLeft
        return currentTop
    }

    open fun bindPost(currentPost: PostWithOwner) {
        textContentView.text = currentPost.post.text
        textGroupView.text = currentPost.getOwnerName()
        textDateView.text = currentPost.post.likes.toString()
        textDateView.text = currentPost.post.getFullStringDate()
        likesPostView.text = currentPost.post.likes.toString()
        commentsPostView.text = currentPost.post.comments.toString()
        commentsPostView.isVisible = currentPost.post.canComment
        sharesPostView.text = currentPost.post.reposts.toString()

        if (currentPost.post.isLiked)
            likesPostView.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_liked
                ), null, null, null
            )
        else
            likesPostView.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_like
                ), null, null, null
            )

        Glide.with(context)
            .load(currentPost.getOwnerPhoto())
            .transform(CenterCrop(), RoundedCorners(100))
            .into(imageGroupView)
    }

    override fun generateLayoutParams(attrs: AttributeSet?) =
        MarginLayoutParams(context, attrs)

    override fun generateDefaultLayoutParams() =
        MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT)
}