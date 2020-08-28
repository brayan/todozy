package br.com.sailboat.todozy.core.presentation.viewholder

import android.view.ViewGroup
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.base.BaseViewHolder
import br.com.sailboat.todozy.core.presentation.model.SubheadView
import kotlinx.android.synthetic.main.vh_subheader.view.*

class SubheaderViewHolder(parent: ViewGroup) :
        BaseViewHolder<SubheadView>(inflate(parent, R.layout.vh_subheader)) {

    override fun bind(item: SubheadView) = with(itemView) {
        vh_subheader__tv__name.text = item.text
    }

}