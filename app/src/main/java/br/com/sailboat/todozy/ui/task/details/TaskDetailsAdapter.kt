package br.com.sailboat.todozy.ui.task.details

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.ui.model.*
import br.com.sailboat.todozy.ui.model.view_holder.*

class TaskDetailsAdapter(private val callback: Callback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface Callback {
        val details: List<ItemView>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ViewType.TITLE.ordinal -> TitleViewHolder(parent)
        ViewType.ALARM.ordinal -> AlarmViewHolder(parent)
        ViewType.LABEL.ordinal -> LabelViewHolder(parent)
        ViewType.LABEL_VALUE.ordinal -> LabelValueViewHolder(parent)
        else -> throw RuntimeException("ViewHolder not found")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = callback.details[position]

        when (item.viewType) {
            ViewType.TITLE.ordinal -> (holder as TitleViewHolder).bind(item as TitleItemView)
            ViewType.ALARM.ordinal -> (holder as AlarmViewHolder).bind(item as AlarmView)
            ViewType.LABEL.ordinal -> (holder as LabelViewHolder).bind(item as LabelItemView)
            ViewType.LABEL_VALUE.ordinal -> (holder as LabelValueViewHolder).bind(item as LabelValueItemView)
            else -> throw RuntimeException("ViewHolder not found")
        }
    }

    override fun getItemCount() = callback.details.size

    override fun getItemViewType(position: Int) = callback.details[position].viewType

}