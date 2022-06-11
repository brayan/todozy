package br.com.sailboat.uicomponent.impl.viewholder

import android.view.ViewGroup
import br.com.sailboat.uicomponent.model.TitleUiModel
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder
import br.com.sailboat.uicomponent.impl.databinding.VhTitleBinding

class TitleViewHolder(parent: ViewGroup) :
    BaseViewHolder<TitleUiModel, VhTitleBinding>(
        VhTitleBinding.inflate(getInflater(parent), parent, false)
    ) {

    override fun bind(item: TitleUiModel) = with(binding) {
        vhTitleTvName.text = item.title
    }

}