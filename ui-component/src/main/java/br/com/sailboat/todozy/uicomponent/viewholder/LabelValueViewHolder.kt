package br.com.sailboat.todozy.uicomponent.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.uicomponent.databinding.VhLabelValueBinding
import br.com.sailboat.todozy.uicomponent.model.LabelValueUiModel
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder

class LabelValueViewHolder(parent: ViewGroup) :
    BaseViewHolder<LabelValueUiModel, VhLabelValueBinding>(
        VhLabelValueBinding.inflate(getInflater(parent), parent, false)
    ) {

    override fun bind(item: LabelValueUiModel) = with(binding) {
        vhLabelValueTvLabel.text = item.label
        vhLabelValueTvValue.text = item.value
    }

}