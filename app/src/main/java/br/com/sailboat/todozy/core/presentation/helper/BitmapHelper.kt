package br.com.sailboat.todozy.core.presentation.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

class BitmapHelper {

    fun getBitmapFromVectorRes(ctx: Context, @DrawableRes vector: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(ctx, vector)

        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

}