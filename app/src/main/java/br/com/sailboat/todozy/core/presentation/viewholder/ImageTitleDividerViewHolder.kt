package br.com.sailboat.todozy.core.presentation.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder
import br.com.sailboat.todozy.core.presentation.model.ImageTitleDividerItemView
import br.com.sailboat.todozy.databinding.VhImageTitleDividerBinding

class ImageTitleDividerViewHolder(parent: ViewGroup) :
    BaseViewHolder<ImageTitleDividerItemView, VhImageTitleDividerBinding>(
        VhImageTitleDividerBinding.inflate(getInflater(parent), parent, false)
    ) {

    override fun bind(item: ImageTitleDividerItemView) = with(binding) {
        vhImageTitleDividerImg.setImageResource(item.imageRes)
        vhImageTitleDividerTvTitle.text = item.title
    }

}