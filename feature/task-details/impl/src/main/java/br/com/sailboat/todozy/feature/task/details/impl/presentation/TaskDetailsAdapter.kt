package br.com.sailboat.todozy.feature.task.details.impl.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.uicomponent.impl.helper.UiModelDiffUtilCallback
import br.com.sailboat.uicomponent.impl.viewholder.AlarmViewHolder
import br.com.sailboat.uicomponent.impl.viewholder.EmptyViewHolder
import br.com.sailboat.uicomponent.impl.viewholder.LabelValueViewHolder
import br.com.sailboat.uicomponent.impl.viewholder.LabelViewHolder
import br.com.sailboat.uicomponent.impl.viewholder.TitleViewHolder
import br.com.sailboat.uicomponent.model.AlarmUiModel
import br.com.sailboat.uicomponent.model.LabelUiModel
import br.com.sailboat.uicomponent.model.LabelValueUiModel
import br.com.sailboat.uicomponent.model.TitleUiModel
import br.com.sailboat.uicomponent.model.UiModel
import br.com.sailboat.uicomponent.model.UiModelType

internal class TaskDetailsAdapter :
    ListAdapter<UiModel, RecyclerView.ViewHolder>(UiModelDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        UiModelType.TITLE.ordinal -> TitleViewHolder(parent)
        UiModelType.ALARM.ordinal -> AlarmViewHolder(parent)
        UiModelType.LABEL.ordinal -> LabelViewHolder(parent)
        UiModelType.LABEL_VALUE.ordinal -> LabelValueViewHolder(parent)
        else -> EmptyViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is TitleUiModel -> (holder as TitleViewHolder).bind(item)
            is AlarmUiModel -> (holder as AlarmViewHolder).bind(item)
            is LabelUiModel -> (holder as LabelViewHolder).bind(item)
            is LabelValueUiModel -> (holder as LabelValueViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).uiModelId
}
