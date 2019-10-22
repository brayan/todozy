package br.com.sailboat.todozy.ui.model.view_holder

import android.view.ViewGroup
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.ui.base.BaseViewHolder
import br.com.sailboat.todozy.ui.model.LabelItemView
import kotlinx.android.synthetic.main.vh_label.view.*

class LabelViewHolder(parent: ViewGroup) :
        BaseViewHolder<LabelItemView>(inflate(parent, R.layout.vh_label)) {

    override fun bind(item: LabelItemView) = with(itemView) {
        vh_label__tv__label.text = item.label
    }

}