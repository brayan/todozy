package br.com.sailboat.todozy.core.presentation.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.uicomponent.databinding.VhImageTitleDividerBinding
import br.com.sailboat.todozy.uicomponent.model.ImageTitleDividerUiModel
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder

class ImageTitleDividerViewHolder(parent: ViewGroup) :
    BaseViewHolder<ImageTitleDividerUiModel, VhImageTitleDividerBinding>(
        VhImageTitleDividerBinding.inflate(getInflater(parent), parent, false)
    ) {

    override fun bind(item: ImageTitleDividerUiModel) = with(binding) {
        vhImageTitleDividerImg.setImageResource(item.imageRes)
        vhImageTitleDividerTvTitle.text = item.title
    }

}