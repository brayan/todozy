package br.com.sailboat.todozy.core.presentation.dialog.colorpicker

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.base.BaseViewHolder
import br.com.sailboat.todozy.core.presentation.helper.gone
import br.com.sailboat.todozy.core.presentation.helper.visible
import kotlinx.android.synthetic.main.vh_color_picker.view.*

class ColorPickerViewHolder(parent: ViewGroup, private val callback: Callback) :
        BaseViewHolder<MaterialColor>(inflate(parent, R.layout.vh_color_picker)) {

    init {
        itemView.view_holder_color_picker__frame.setOnClickListener {
            callback.onClickColor(adapterPosition)
        }
    }

    override fun bind(item: MaterialColor) = with(itemView) {
        view_holder_color_picker__img__color.setColorFilter(getTextColor(item.ordinal))

        if (callback.currentColor == item) {
            view_holder_color_picker__img__check.visible()
        } else {
            view_holder_color_picker__img__check.gone()
        }

    }

    private fun getTextColor(colorId: Int): Int {
        return ContextCompat.getColor(itemView.context, colorId)
    }


    interface Callback {
        val currentColor: MaterialColor?
        fun onClickColor(adapterPosition: Int)
    }
}