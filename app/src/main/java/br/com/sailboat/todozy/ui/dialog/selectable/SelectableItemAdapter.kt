package br.com.sailboat.todozy.ui.dialog.selectable

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SelectableItemAdapter(private val callback: Callback) : RecyclerView.Adapter<SelectableItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableItemViewHolder {
        return SelectableItemViewHolder(parent, callback)
    }

    override fun onBindViewHolder(holder: SelectableItemViewHolder, position: Int) {
        val item = callback.selectableItems[position]
        holder.bind(item)
    }

    override fun getItemCount() = callback.selectableItems.size

    interface Callback : SelectableItemViewHolder.Callback {
        val selectableItems: List<SelectableItem>
    }

}