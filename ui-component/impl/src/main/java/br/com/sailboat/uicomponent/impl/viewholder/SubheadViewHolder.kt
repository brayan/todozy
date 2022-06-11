package br.com.sailboat.uicomponent.impl.viewholder

import android.view.ViewGroup
import br.com.sailboat.uicomponent.model.SubheadUiModel
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder
import br.com.sailboat.uicomponent.impl.databinding.VhSubheaderBinding

class SubheadViewHolder(parent: ViewGroup) :
    BaseViewHolder<SubheadUiModel, VhSubheaderBinding>(
        VhSubheaderBinding.inflate(getInflater(parent), parent, false)
    ) {

    override fun bind(item: SubheadUiModel) = with(binding) {
        vhSubheaderTvName.text = context.getText(item.subheadRes)
    }

}