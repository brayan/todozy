package br.com.sailboat.todozy.core.presentation.helper

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

class AnimationHelper {

    fun performScaleUpAnimation(view: View) {
        view.animate().scaleX(1f)
            .scaleY(1f).interpolator = FastOutSlowInInterpolator()
    }

    fun performScaleDownAnimation(view: View) {
        view.animate().scaleX(0f)
            .scaleY(0f).interpolator = FastOutSlowInInterpolator()
    }

    fun expand(v: View) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetHeight = v.measuredHeight

        v.layoutParams.height = 1
        v.visibility = View.VISIBLE

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                v.layoutParams.height = if (interpolatedTime == 1f)
                    ViewGroup.LayoutParams.WRAP_CONTENT
                else
                    (targetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        a.duration = (targetHeight / v.context.resources.displayMetrics.density).toInt().toLong()
        v.startAnimation(a)
    }

    fun collapse(v: View) {
        val initialHeight = v.measuredHeight

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // 1dp/ms
        a.duration = (initialHeight / v.context.resources.displayMetrics.density).toInt().toLong()
        v.startAnimation(a)
    }

    fun performAnimationBackgroundColor(
        ctx: Context,
        colorFrom: Int,
        colorTo: Int,
        duration: Int,
        callback: BackgroundColorCallback
    ) {
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = duration.toLong()
        colorAnimation.addUpdateListener { animator -> callback.onAnimationUpdate(animator) }
        colorAnimation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                callback.onAnimationEnd(animation)
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        colorAnimation.start()
    }


    interface BackgroundColorCallback {
        fun onAnimationUpdate(animator: ValueAnimator)
        fun onAnimationEnd(animation: Animator)
    }
}