package br.com.sailboat.todozy.ui.helper

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.ui.model.view_holder.TaskViewHolder

class SwipeTaskLeftRight(private val context: Context, private val callback: Callback) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private var paint = Paint()
    private var paint2 = Paint()
    private var bitmapThumbUp = BitmapFactory.decodeResource(context.resources, R.drawable.ic_thumb_up_white_24dp)
    private var bitmapThumbDown = BitmapFactory.decodeResource(context.resources, R.drawable.ic_thumb_down_white_24dp)

    interface Callback {
        fun onSwiped(position: Int, direction: Int)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView,
                             viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                             actionState: Int, isCurrentlyActive: Boolean) {

        paint2.color = ContextCompat.getColor(getContext()!!, android.R.color.black)
        paint2.textSize = 48f

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            val itemView = viewHolder.itemView

            if (isSwipedToRight(dX)) {
                paint.color = ContextCompat.getColor(getContext()!!, R.color.md_teal_300)

                val height = (itemView.height / 2 - bitmapThumbUp.height / 2).toFloat()
                val bitmapWidth = bitmapThumbUp.width.toFloat()

                var rightValue = itemView.right.toFloat()

                if (itemView.left + dX < rightValue) {
                    rightValue = itemView.left + dX
                }

                c.drawRect(itemView.left.toFloat(),
                        itemView.top.toFloat(),
                        rightValue,
                        itemView.bottom.toFloat(), paint)

                c.drawBitmap(bitmapThumbUp, itemView.left.toFloat() + bitmapWidth - 14f,
                        itemView.top.toFloat() + height, null)

            } else {
                paint.color = ContextCompat.getColor(getContext()!!, R.color.md_red_300)

                val height = (itemView.height / 2 - bitmapThumbDown.height / 2).toFloat()
                val bitmapWidth = bitmapThumbDown.width.toFloat()

                var leftValue = itemView.left.toFloat()

                if (itemView.right + dX > leftValue) {
                    leftValue = itemView.right + dX
                }

                c.drawRect(leftValue,
                        itemView.top.toFloat(),
                        itemView.right.toFloat(),
                        itemView.bottom.toFloat(), paint)

                c.drawBitmap(bitmapThumbDown, itemView.right.toFloat() - bitmapWidth - 30f,
                        itemView.top.toFloat() + height, null)
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        if (shouldAllowSwipe(viewHolder)) {
            val view = viewHolder as TaskViewHolder
            callback.onSwiped(view.adapterPosition, direction)
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

    fun getContext(): Context? {
        return context
    }

    private fun isSwipedToRight(dX: Float): Boolean {
        return dX > 0
    }

    private fun shouldAllowSwipe(viewHolder: RecyclerView.ViewHolder): Boolean {
        return viewHolder is TaskViewHolder
    }

}