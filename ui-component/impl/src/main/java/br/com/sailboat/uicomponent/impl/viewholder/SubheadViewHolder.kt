package br.com.sailboat.uicomponent.impl.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder
import br.com.sailboat.uicomponent.impl.databinding.VhSubheaderBinding
import br.com.sailboat.uicomponent.model.SubheadUiModel

class SubheadViewHolder(parent: ViewGroup) :
    BaseViewHolder<SubheadUiModel, VhSubheaderBinding>(
        VhSubheaderBinding.inflate(getInflater(parent), parent, false),
    ) {
    override fun bind(item: SubheadUiModel) = with(binding) {
        vhSubheaderTvName.text = item.subhead
    }
}
