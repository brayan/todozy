package br.com.sailboat.todozy.uicomponent.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.uicomponent.databinding.VhTitleBinding
import br.com.sailboat.todozy.uicomponent.model.TitleUiModel
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder

class TitleViewHolder(parent: ViewGroup) :
    BaseViewHolder<TitleUiModel, VhTitleBinding>(
        VhTitleBinding.inflate(getInflater(parent), parent, false)
    ) {

    override fun bind(item: TitleUiModel) = with(binding) {
        vhTitleTvName.text = item.title
    }

}