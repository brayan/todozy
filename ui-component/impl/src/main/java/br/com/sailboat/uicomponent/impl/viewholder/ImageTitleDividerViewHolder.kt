package br.com.sailboat.uicomponent.impl.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder
import br.com.sailboat.uicomponent.impl.databinding.VhImageTitleDividerBinding
import br.com.sailboat.uicomponent.model.ImageTitleDividerUiModel

class ImageTitleDividerViewHolder(parent: ViewGroup) :
    BaseViewHolder<ImageTitleDividerUiModel, VhImageTitleDividerBinding>(
        VhImageTitleDividerBinding.inflate(getInflater(parent), parent, false),
    ) {
    override fun bind(item: ImageTitleDividerUiModel) =
        with(binding) {
            vhImageTitleDividerImg.setImageResource(item.imageRes)
            vhImageTitleDividerTvTitle.text = item.title
        }
}
