package me.szymanski.arch

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.View

object Animations {
    fun View.animate(getFrom: (view: View) -> Float, to: Float, update: (Float) -> Unit): Animator =
        ValueAnimator.ofFloat(getFrom(this), to).apply {
            addUpdateListener { update(it.animatedValue as Float) }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(p0: Animator?) = update(to)
                override fun onAnimationStart(p0: Animator?) = Unit
                override fun onAnimationCancel(p0: Animator?) = Unit
                override fun onAnimationRepeat(p0: Animator?) = Unit
            })
        }

    fun playTogether(vararg animators: Animator): Animator = AnimatorSet().apply {
        playTogether(*animators)
        start()
    }

    fun View.animateAlpha(to: Float) = animate({ alpha }, to, { alpha = it })
}
