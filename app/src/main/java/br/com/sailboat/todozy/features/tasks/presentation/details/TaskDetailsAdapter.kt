package br.com.sailboat.todozy.features.tasks.presentation.details

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.core.presentation.model.AlarmView
import br.com.sailboat.todozy.core.presentation.viewholder.AlarmViewHolder
import br.com.sailboat.todozy.core.presentation.viewholder.LabelValueViewHolder
import br.com.sailboat.todozy.core.presentation.viewholder.LabelViewHolder
import br.com.sailboat.todozy.core.presentation.viewholder.TitleViewHolder
import br.com.sailboat.todozy.uicomponent.model.*

class TaskDetailsAdapter(private val callback: Callback) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface Callback {
        val details: List<UiModel>
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

        when (item.index) {
            ViewType.TITLE.ordinal -> (holder as TitleViewHolder).bind(item as TitleUiModel)
            ViewType.ALARM.ordinal -> (holder as AlarmViewHolder).bind(item as AlarmView)
            ViewType.LABEL.ordinal -> (holder as LabelViewHolder).bind(item as LabelUiModel)
            ViewType.LABEL_VALUE.ordinal -> (holder as LabelValueViewHolder).bind(item as LabelValueUiModel)
            else -> throw RuntimeException("ViewHolder not found")
        }
    }

    override fun getItemCount() = callback.details.size

    override fun getItemViewType(position: Int) = callback.details[position].index

}