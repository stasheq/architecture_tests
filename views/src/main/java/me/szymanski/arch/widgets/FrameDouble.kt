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

    private fun View.animateWidth(to: Float) = animate({ getPWidth() }, to, { setPWidth(it) })
    private fun View.getPWidth() = (layoutParams as ConstraintLayout.LayoutParams).matchConstraintPercentWidth
    private fun View.setPWidth(value: Float) {
        val lp = layoutParams as ConstraintLayout.LayoutParams
        lp.matchConstraintPercentWidth = value
        layoutParams = lp
    }

    private val leftWidth = 0.3f
    private val rightWidth = 0.7f
    private var animator: Animator? = null

    enum class State { LEFT, RIGHT, BOTH }

    fun setState(state: State, animate: Boolean = true) {
        animator?.cancel()
        animator = null
        when (state) {
            State.LEFT -> {
                leftColumn.setPWidth(1f)
                if (animate) {
                    animator = playTogether(rightColumn.animateWidth(0f), cover.animateAlpha(0f))
                } else {
                    cover.alpha = 0f
                    rightColumn.setPWidth(0f)
                }
            }
            State.RIGHT -> {
                leftColumn.setPWidth(1f)
                if (animate) {
                    animator = playTogether(rightColumn.animateWidth(1f), cover.animateAlpha(1f))
                } else {
                    cover.alpha = 1f
                    rightColumn.setPWidth(1f)
                }
            }
            State.BOTH -> {
                leftColumn.setPWidth(leftWidth)
                rightColumn.setPWidth(rightWidth)
                cover.alpha = 0f
            }
        }
    }
}
