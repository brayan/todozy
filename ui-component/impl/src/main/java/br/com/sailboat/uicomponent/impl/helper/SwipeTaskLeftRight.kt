package br.com.sailboat.uicomponent.impl.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.DisplayMetrics
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.uicomponent.impl.R
import br.com.sailboat.uicomponent.impl.viewholder.TaskViewHolder
import kotlin.math.roundToInt

class SwipeTaskLeftRight(private val context: Context, private val callback: Callback) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    private var paint = Paint()

    interface Callback {
        fun onSwipeLeft(position: Int)
        fun onSwipeRight(position: Int)
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {
        val icon: Bitmap

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView

            if (isSwipedToRight(dX)) {
                paint.color = ContextCompat.getColor(context, R.color.md_teal_200)

                var rightValue = itemView.right.toFloat()

                if (itemView.left + dX < rightValue) {
                    rightValue = itemView.left + dX
                }

                canvas.drawRect(
                    itemView.left.toFloat(),
                    itemView.top.toFloat(),
                    rightValue,
                    itemView.bottom.toFloat(),
                    paint,
                )

                icon = getBitmapFromVectorDrawable(R.drawable.ic_vec_thumb_up_white_24dp)

                canvas.drawBitmap(
                    icon,
                    itemView.left.toFloat() + convertDpToPx(16),
                    itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height) / 2,
                    paint,
                )
            } else {
                paint.color = ContextCompat.getColor(context, R.color.md_red_200)

                var leftValue = itemView.left.toFloat()

                if (itemView.right + dX > leftValue) {
                    leftValue = itemView.right + dX
                }

                canvas.drawRect(
                    leftValue,
                    itemView.top.toFloat(),
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat(),
                    paint,
                )

                icon = getBitmapFromVectorDrawable(R.drawable.ic_vect_thumb_down_white_24dp)

                canvas.drawBitmap(
                    icon,
                    itemView.right.toFloat() - convertDpToPx(16) - icon.width,
                    itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height) / 2,
                    paint,
                )
            }

            super.onChildDraw(
                canvas,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive,
            )
        }
    }

    override fun onSwiped(
        viewHolder: RecyclerView.ViewHolder,
        direction: Int,
    ) {
        if (shouldAllowSwipe(viewHolder)) {
            val view = viewHolder as TaskViewHolder

            if (direction == ItemTouchHelper.LEFT) {
                callback.onSwipeLeft(view.bindingAdapterPosition)
            } else {
                callback.onSwipeRight(view.bindingAdapterPosition)
            }
        }
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Int {
        return if (!shouldAllowSwipe(viewHolder)) {
            0
        } else {
            super.getSwipeDirs(recyclerView, viewHolder)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
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

    private fun convertDpToPx(dp: Int): Int {
        return (dp * (context.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    private fun getBitmapFromVectorDrawable(drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val bitmap =
            Bitmap.createBitmap(
                drawable!!.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888,
            )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }
}
