package me.szymanski.arch.widgets

import android.animation.Animator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import me.szymanski.arch.Animations.animate
import me.szymanski.arch.Animations.animateAlpha
import me.szymanski.arch.Animations.playTogether
import me.szymanski.arch.ViewWidget
import me.szymanski.arch.measure
import me.szymanski.arch.widgets.databinding.FrameDoubleBinding

class FrameDouble(ctx: Context) : ViewWidget {
    val rightColumn: ViewGroup
    val leftColumn: ViewGroup
    private val cover: View

    override val root = FrameDoubleBinding.inflate(LayoutInflater.from(ctx)).apply {
        rightColumn = columnRight
        leftColumn = columnLeft
        cover = columnCover
    }.root

    private fun View.animateTranslation(to: Float) = animate({ translationX }, to, { translationX = it })
    private fun View.setPWidth(value: Float) {
        val lp = layoutParams as ConstraintLayout.LayoutParams
        lp.matchConstraintPercentWidth = value
        layoutParams = lp
    }

    private var animator: Animator? = null

    enum class State { LEFT, RIGHT, BOTH }

    fun setState(state: State, animate: Boolean = true) {
        val animationEnabled = animate && rightColumn.isAttachedToWindow && rightColumn.width > 0
        animator?.cancel()
        animator = null
        when (state) {
            State.LEFT -> {
                leftColumn.setPWidth(1f)
                rightColumn.setPWidth(1f)
                if (animationEnabled) {
                    animator = playTogether(
                        rightColumn.animateTranslation(rightColumn.width.toFloat()),
                        cover.animateAlpha(0f)
                    )
                } else {
                    cover.alpha = 0f
                    rightColumn.measure { width, _ -> rightColumn.translationX = width.toFloat() }
                }
            }
            State.RIGHT -> {
                leftColumn.setPWidth(1f)
                rightColumn.setPWidth(1f)
                if (animationEnabled) {
                    animator = playTogether(rightColumn.animateTranslation(0f), cover.animateAlpha(1f))
                } else {
                    cover.alpha = 1f
                    rightColumn.translationX = 0f
                }
            }
            State.BOTH -> {
                leftColumn.setPWidth(0.3f)
                rightColumn.setPWidth(0.7f)
                cover.alpha = 0f
                rightColumn.translationX = 0f
            }
        }
    }
}
