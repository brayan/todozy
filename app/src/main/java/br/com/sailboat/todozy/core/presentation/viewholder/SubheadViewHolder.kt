package br.com.sailboat.todozy.core.presentation.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder
import br.com.sailboat.todozy.core.presentation.model.SubheadView
import br.com.sailboat.todozy.databinding.VhSubheaderBinding

class SubheadViewHolder(parent: ViewGroup) :
    BaseViewHolder<SubheadView, VhSubheaderBinding>(
        VhSubheaderBinding.inflate(getInflater(parent), parent, false)
    ) {

    override fun bind(item: SubheadView) = with(binding) {
        vhSubheaderTvName.text = context.getText(item.subheadRes)
    }

}