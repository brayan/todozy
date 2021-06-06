package br.com.sailboat.todozy.core.presentation.dialog.colorpicker

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import br.com.sailboat.todozy.core.presentation.base.BaseViewHolder
import br.com.sailboat.todozy.core.presentation.helper.gone
import br.com.sailboat.todozy.core.presentation.helper.visible
import br.com.sailboat.todozy.databinding.VhColorPickerBinding

class ColorPickerViewHolder(parent: ViewGroup, private val callback: Callback) :
    BaseViewHolder<MaterialColor, VhColorPickerBinding>(
        VhColorPickerBinding.inflate(getInflater(parent), parent, false)
    ) {

    init {
        binding.viewHolderColorPickerFrame.setOnClickListener {
            callback.onClickColor(bindingAdapterPosition)
        }
    }

    override fun bind(item: MaterialColor) = with(binding) {
        viewHolderColorPickerImgColor.setColorFilter(getTextColor(item.ordinal))

        if (callback.currentColor == item) {
            viewHolderColorPickerImgCheck.visible()
        } else {
            viewHolderColorPickerImgCheck.gone()
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