package br.com.sailboat.uicomponent.impl.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder
import br.com.sailboat.uicomponent.impl.databinding.VhLabelValueBinding
import br.com.sailboat.uicomponent.model.LabelValueUiModel

class LabelValueViewHolder(parent: ViewGroup) :
    BaseViewHolder<LabelValueUiModel, VhLabelValueBinding>(
        VhLabelValueBinding.inflate(getInflater(parent), parent, false),
    ) {
    override fun bind(item: LabelValueUiModel) =
        with(binding) {
            vhLabelValueTvLabel.text = item.label
            vhLabelValueTvValue.text = item.value
        }
}
