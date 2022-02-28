package br.com.sailboat.todozy.uicomponent.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.uicomponent.databinding.VhSubheaderBinding
import br.com.sailboat.todozy.uicomponent.model.SubheadUiModel
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder

class SubheadViewHolder(parent: ViewGroup) :
    BaseViewHolder<SubheadUiModel, VhSubheaderBinding>(
        VhSubheaderBinding.inflate(getInflater(parent), parent, false)
    ) {

    override fun bind(item: SubheadUiModel) = with(binding) {
        vhSubheaderTvName.text = context.getText(item.subheadRes)
    }

}