package br.com.sailboat.todozy.core.presentation.dialog.selectable

import android.view.ViewGroup
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.base.BaseViewHolder
import br.com.sailboat.todozy.core.presentation.helper.gone
import br.com.sailboat.todozy.core.presentation.helper.visible
import kotlinx.android.synthetic.main.vh_selectable_item.view.*

class SelectableItemViewHolder(parent: ViewGroup, private val callback: Callback) :
        BaseViewHolder<SelectableItem>(inflate(parent, R.layout.vh_selectable_item)) {


    init {
        itemView.setOnClickListener { callback.onClickItem(adapterPosition) }
    }


    override fun bind(item: SelectableItem) = with(itemView) {
        vh_selectable_item__tv__name.setText(item.getName())

        val selectedItem = callback.selectedItem
        if (selectedItem != null && selectedItem === item) {
            vh_selectable_item__img__selected.visible()
        } else {
            vh_selectable_item__img__selected.gone()
        }

    }


    interface Callback {
        val selectedItem: SelectableItem?
        fun onClickItem(position: Int)
    }


}