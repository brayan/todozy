package br.com.sailboat.todozy.core.presentation.dialog.colorpicker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ColorPickerAdapter(private val callback: ColorPickerViewHolder.Callback) :
    RecyclerView.Adapter<ColorPickerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorPickerViewHolder {
        return ColorPickerViewHolder(parent, callback)
    }

    override fun onBindViewHolder(holder: ColorPickerViewHolder, position: Int) {
        val color = MaterialColor.getColor(position)
        holder.bind(color)
    }

    override fun getItemCount() = MaterialColor.values().size

}