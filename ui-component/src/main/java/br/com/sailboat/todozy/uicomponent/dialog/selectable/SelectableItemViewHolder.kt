package br.com.sailboat.todozy.uicomponent.dialog.selectable

import android.view.ViewGroup
import br.com.sailboat.todozy.uicomponent.databinding.VhSelectableItemBinding
import br.com.sailboat.todozy.uicomponent.dialog.selectable.model.SelectableItem
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.visible

class SelectableItemViewHolder(parent: ViewGroup, private val callback: Callback) :
    BaseViewHolder<SelectableItem, VhSelectableItemBinding>(
        VhSelectableItemBinding.inflate(getInflater(parent), parent, false)
    ) {


    init {
        itemView.setOnClickListener { callback.onClickItem(adapterPosition) }
    }


    override fun bind(item: SelectableItem) = with(binding) {
        vhSelectableItemTvName.setText(item.getName())

        val selectedItem = callback.selectedItem
        if (selectedItem != null && selectedItem === item) {
            vhSelectableItemImgSelected.visible()
        } else {
            vhSelectableItemImgSelected.gone()
        }

    }


    interface Callback {
        val selectedItem: SelectableItem?
        fun onClickItem(position: Int)
    }


}