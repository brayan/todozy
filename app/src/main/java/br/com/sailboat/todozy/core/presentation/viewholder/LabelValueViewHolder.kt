package br.com.sailboat.todozy.core.presentation.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder
import br.com.sailboat.todozy.core.presentation.model.LabelValueItemView
import br.com.sailboat.todozy.databinding.VhLabelValueBinding

class LabelValueViewHolder(parent: ViewGroup) :
    BaseViewHolder<LabelValueItemView, VhLabelValueBinding>(
        VhLabelValueBinding.inflate(getInflater(parent), parent, false)
    ) {

    override fun bind(item: LabelValueItemView) = with(binding) {
        vhLabelValueTvLabel.text = item.label
        vhLabelValueTvValue.text = item.value
    }

}