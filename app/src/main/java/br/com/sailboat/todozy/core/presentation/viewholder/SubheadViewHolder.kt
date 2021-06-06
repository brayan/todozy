package br.com.sailboat.todozy.core.presentation.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.core.presentation.base.BaseViewHolder
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