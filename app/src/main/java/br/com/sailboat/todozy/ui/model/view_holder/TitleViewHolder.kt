package br.com.sailboat.todozy.ui.model.view_holder

import android.view.ViewGroup
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.ui.base.BaseViewHolder
import br.com.sailboat.todozy.ui.model.TitleItemView
import kotlinx.android.synthetic.main.vh_title.view.*

class TitleViewHolder(parent: ViewGroup) : BaseViewHolder<TitleItemView>(inflate(parent, R.layout.vh_title)) {

    override fun bind(item: TitleItemView) = with(itemView) {
        vh_title__tv__name.text = item.title
    }

}