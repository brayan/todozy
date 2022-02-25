package br.com.sailboat.todozy.core.presentation.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.databinding.VhLabelBinding
import br.com.sailboat.todozy.uicomponent.model.LabelUiModel
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder

class LabelViewHolder(parent: ViewGroup) : BaseViewHolder<LabelUiModel, VhLabelBinding>(
    VhLabelBinding.inflate(getInflater(parent), parent, false)
) {

    override fun bind(item: LabelUiModel) = with(binding) {
        vhLabelTvLabel.text = item.label
    }

}