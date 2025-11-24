package br.com.sailboat.uicomponent.impl.helper

import android.content.Context
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class ImageViewColorHelper {
    fun setColorOfVector(
        context: Context,
        imageView: ImageView,
        colorId: Int,
    ) {
        imageView.setColorFilter(
            ContextCompat.getColor(context, colorId),
            android.graphics.PorterDuff.Mode.SRC_IN,
        )
    }

    fun setColorOfImage(
        context: Context,
        imageView: ImageView,
        colorId: Int,
    ) {
        imageView.setColorFilter(
            ContextCompat.getColor(context, colorId),
            android.graphics.PorterDuff.Mode.MULTIPLY,
        )
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun setBackgroundTint(
        context: Context,
        imageView: ImageView,
        colorId: Int,
    ) {
        imageView.backgroundTintList = ContextCompat.getColorStateList(context, colorId)
    }
}
