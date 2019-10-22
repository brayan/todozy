package br.com.sailboat.todozy.ui.model.view_holder

import android.view.ViewGroup
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.ui.base.BaseViewHolder
import br.com.sailboat.todozy.ui.helper.*
import br.com.sailboat.todozy.ui.model.SubheadView
import br.com.sailboat.todozy.ui.model.TaskItemView
import kotlinx.android.synthetic.main.task.view.*
import kotlinx.android.synthetic.main.vh_subheader.view.*
import java.util.*

class SubheaderViewHolder(parent: ViewGroup) :
        BaseViewHolder<SubheadView>(inflate(parent, R.layout.vh_subheader)) {

    override fun bind(item: SubheadView) = with(itemView) {
        vh_subheader__tv__name.text = item.text
    }

}