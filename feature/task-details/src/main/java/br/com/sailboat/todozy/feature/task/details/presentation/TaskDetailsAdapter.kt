package br.com.sailboat.todozy.feature.task.details.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.uicomponent.model.*
import br.com.sailboat.todozy.uicomponent.viewholder.AlarmViewHolder
import br.com.sailboat.todozy.uicomponent.viewholder.LabelValueViewHolder
import br.com.sailboat.todozy.uicomponent.viewholder.LabelViewHolder
import br.com.sailboat.todozy.uicomponent.viewholder.TitleViewHolder

class TaskDetailsAdapter(private val callback: Callback) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface Callback {
        val details: List<UiModel>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        UiModelType.TITLE.ordinal -> TitleViewHolder(parent)
        UiModelType.ALARM.ordinal -> AlarmViewHolder(parent)
        UiModelType.LABEL.ordinal -> LabelViewHolder(parent)
        UiModelType.LABEL_VALUE.ordinal -> LabelValueViewHolder(parent)
        else -> throw RuntimeException("ViewHolder not found")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = callback.details[position]

        when (item.index) {
            UiModelType.TITLE.ordinal -> (holder as TitleViewHolder).bind(item as TitleUiModel)
            UiModelType.ALARM.ordinal -> (holder as AlarmViewHolder).bind(item as AlarmUiModel)
            UiModelType.LABEL.ordinal -> (holder as LabelViewHolder).bind(item as LabelUiModel)
            UiModelType.LABEL_VALUE.ordinal -> (holder as LabelValueViewHolder).bind(item as LabelValueUiModel)
            else -> throw RuntimeException("ViewHolder not found")
        }
    }

    override fun getItemCount() = callback.details.size

    override fun getItemViewType(position: Int) = callback.details[position].index

}