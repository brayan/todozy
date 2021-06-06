package br.com.sailboat.todozy.core.presentation.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.core.presentation.base.BaseViewHolder
import br.com.sailboat.todozy.core.presentation.model.TitleItemView
import br.com.sailboat.todozy.databinding.VhTitleBinding

class TitleViewHolder(parent: ViewGroup) :
    BaseViewHolder<TitleItemView, VhTitleBinding>(
        VhTitleBinding.inflate(getInflater(parent), parent, false)
    ) {

    override fun bind(item: TitleItemView) = with(binding) {
        vhTitleTvName.text = item.title
    }

}