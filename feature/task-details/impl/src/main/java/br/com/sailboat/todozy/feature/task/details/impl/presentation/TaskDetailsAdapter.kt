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
        val item = getItem(position)

        when (item.uiModelId) {
            UiModelType.TITLE.ordinal -> (holder as TitleViewHolder).bind(item as TitleUiModel)
            UiModelType.ALARM.ordinal -> (holder as AlarmViewHolder).bind(item as AlarmUiModel)
            UiModelType.LABEL.ordinal -> (holder as LabelViewHolder).bind(item as LabelUiModel)
            UiModelType.LABEL_VALUE.ordinal -> (holder as LabelValueViewHolder).bind(item as LabelValueUiModel)
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).uiModelId
}
