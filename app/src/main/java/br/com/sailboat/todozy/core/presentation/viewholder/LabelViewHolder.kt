package br.com.sailboat.todozy.core.presentation.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder
import br.com.sailboat.todozy.core.presentation.model.LabelItemView
import br.com.sailboat.todozy.databinding.VhLabelBinding

class LabelViewHolder(parent: ViewGroup) : BaseViewHolder<LabelItemView, VhLabelBinding>(
    VhLabelBinding.inflate(getInflater(parent), parent, false)
) {

    override fun bind(item: LabelItemView) = with(binding) {
        vhLabelTvLabel.text = item.label
    }

}