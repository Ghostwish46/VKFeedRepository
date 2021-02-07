package dev.ghost.homework_2.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.view.*
import kotlin.math.max

class FlexboxLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attributeSet, defStyleAttr) {

    init {
        setWillNotDraw(true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = MeasureSpec.getSize(widthMeasureSpec)
        var height = 0
        var currentRowWidth = 0

        children.forEach { child ->
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, height)
            currentRowWidth += child.measuredWidth

            if (currentRowWidth > desiredWidth) {
                currentRowWidth = 0
                height += child.measuredHeight
            } else {
                height = max(height, child.measuredHeight)
            }
        }

        setMeasuredDimension(desiredWidth, View.resolveSize(height, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentLeft = paddingLeft
        var currentTop = t + paddingTop

        var tempMaxMeasuredHeight = 0

        children.forEach { child ->
            currentLeft += child.marginLeft
            val currentRight = currentLeft + child.measuredWidth
            if (currentRight > measuredWidth - paddingRight && currentLeft != paddingLeft) {
                currentLeft = paddingLeft + child.marginLeft
                currentTop += tempMaxMeasuredHeight
                tempMaxMeasuredHeight = 0
            }
            tempMaxMeasuredHeight = max(
                tempMaxMeasuredHeight,
                child.measuredHeight + child.marginTop + child.marginBottom
            )

            child.layout(
                currentLeft,
                currentTop + child.marginTop,
                currentRight,
                currentTop + child.marginTop + child.measuredHeight
            )
            currentLeft += child.measuredWidth + child.marginRight

        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?) =
        MarginLayoutParams(context, attrs)

    override fun generateDefaultLayoutParams() = MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT)
}