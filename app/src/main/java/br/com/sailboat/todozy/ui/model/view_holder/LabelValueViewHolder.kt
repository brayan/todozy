package br.com.sailboat.todozy.ui.model.view_holder

import android.view.ViewGroup
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.ui.base.BaseViewHolder
import br.com.sailboat.todozy.ui.model.LabelValueItemView
import kotlinx.android.synthetic.main.vh_label_value.view.*

class LabelValueViewHolder(parent: ViewGroup) :
        BaseViewHolder<LabelValueItemView>(inflate(parent, R.layout.vh_label_value)) {

    override fun bind(item: LabelValueItemView) = with(itemView) {
        vh_label_value__tv__label.text = item.label
        vh_label_value__tv__value.text = item.value
    }

}