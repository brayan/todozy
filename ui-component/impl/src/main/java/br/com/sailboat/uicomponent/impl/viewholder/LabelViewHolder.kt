package br.com.sailboat.uicomponent.impl.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder
import br.com.sailboat.uicomponent.impl.databinding.VhLabelBinding
import br.com.sailboat.uicomponent.model.LabelUiModel

class LabelViewHolder(parent: ViewGroup) : BaseViewHolder<LabelUiModel, VhLabelBinding>(
    VhLabelBinding.inflate(getInflater(parent), parent, false)
) {

    override fun bind(item: LabelUiModel) = with(binding) {
        vhLabelTvLabel.text = item.label
    }
}
