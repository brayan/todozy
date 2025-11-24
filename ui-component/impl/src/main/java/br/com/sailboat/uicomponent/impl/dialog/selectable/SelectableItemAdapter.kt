package br.com.sailboat.uicomponent.impl.dialog.selectable

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.SelectableItem

class SelectableItemAdapter(private val callback: Callback) :
    ListAdapter<SelectableItem, SelectableItemViewHolder>(
        SelectableItemDiffUtilCallback(),
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SelectableItemViewHolder {
        return SelectableItemViewHolder(parent, callback)
    }

    override fun onBindViewHolder(
        holder: SelectableItemViewHolder,
        position: Int,
    ) {
        val item = callback.selectableItems[position]
        holder.bind(item)
    }

    override fun getItemCount() = callback.selectableItems.size

    interface Callback : SelectableItemViewHolder.Callback {
        val selectableItems: List<SelectableItem>
    }

    private class SelectableItemDiffUtilCallback : DiffUtil.ItemCallback<SelectableItem>() {
        override fun areItemsTheSame(
            oldItem: SelectableItem,
            newItem: SelectableItem,
        ) = oldItem.getName() == newItem.getName()

        override fun areContentsTheSame(
            oldItem: SelectableItem,
            newItem: SelectableItem,
        ) = oldItem.getName() == newItem.getName()
    }
}
