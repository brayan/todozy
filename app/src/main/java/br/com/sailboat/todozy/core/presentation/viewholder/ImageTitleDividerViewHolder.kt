package br.com.sailboat.todozy.core.presentation.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.base.BaseViewHolder
import br.com.sailboat.todozy.core.presentation.model.ImageTitleDividerItemView
import kotlinx.android.synthetic.main.vh_image_title_divider.view.*

class ImageTitleDividerViewHolder(parent: ViewGroup) :
        BaseViewHolder<ImageTitleDividerItemView>(inflate(parent, R.layout.vh_image_title_divider)) {

    override fun bind(item: ImageTitleDividerItemView) = with(itemView) {
        vh_image_title_divider__img.setImageResource(item.imageRes)
        vh_image_title_divider__tv__title.text = item.title
    }

}