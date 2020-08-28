package br.com.sailboat.todozy.core.presentation.helper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.viewholder.TaskViewHolder

class SwipeTaskLeftRight(private val context: Context, private val callback: Callback) :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private var paint = Paint()
    private var paint2 = Paint()

    interface Callback {
        fun onSwipeLeft(position: Int)
        fun onSwipeRight(position: Int)
    }

    override fun onChildDraw(canvas: Canvas, recyclerView: RecyclerView,
                             viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                             actionState: Int, isCurrentlyActive: Boolean) {

        paint2.color = ContextCompat.getColor(context, android.R.color.black)
        paint2.textSize = 48f

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            val itemView = viewHolder.itemView

            if (isSwipedToRight(dX)) {
                paint.color = ContextCompat.getColor(context, R.color.md_teal_200)

                var rightValue = itemView.right.toFloat()

                if (itemView.left + dX < rightValue) {
                    rightValue = itemView.left + dX
                }

                canvas.drawRect(itemView.left.toFloat(),
                        itemView.top.toFloat(),
                        rightValue,
                        itemView.bottom.toFloat(), paint)

            } else {
                paint.color = ContextCompat.getColor(context, R.color.md_red_200)


                var leftValue = itemView.left.toFloat()

                if (itemView.right + dX > leftValue) {
                    leftValue = itemView.right + dX
                }

                canvas.drawRect(leftValue,
                        itemView.top.toFloat(),
                        itemView.right.toFloat(),
                        itemView.bottom.toFloat(), paint)
            }

            super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        if (shouldAllowSwipe(viewHolder)) {
            val view = viewHolder as TaskViewHolder

            if (direction == ItemTouchHelper.LEFT) {
                callback.onSwipeLeft(view.adapterPosition)
            } else {
                callback.onSwipeRight(view.adapterPosition)
            }
        }

    }

    override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return if (!shouldAllowSwipe(viewHolder)) {
            0
        } else super.getSwipeDirs(recyclerView, viewHolder)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return super.getSwipeEscapeVelocity(defaultValue * 3)
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return super.getSwipeVelocityThreshold(defaultValue * 2)
    }

    private fun isSwipedToRight(dX: Float): Boolean {
        return dX > 0
    }

    private fun shouldAllowSwipe(viewHolder: RecyclerView.ViewHolder): Boolean {
        return viewHolder is TaskViewHolder
    }

}