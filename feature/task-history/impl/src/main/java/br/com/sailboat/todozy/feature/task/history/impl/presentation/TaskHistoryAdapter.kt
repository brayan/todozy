package br.com.sailboat.todozy.feature.task.history.impl.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.uicomponent.impl.formatter.TaskHistoryDateTimeFormatter
import br.com.sailboat.uicomponent.impl.helper.UiModelDiffUtilCallback
import br.com.sailboat.uicomponent.impl.viewholder.EmptyViewHolder
import br.com.sailboat.uicomponent.impl.viewholder.SubheadViewHolder
import br.com.sailboat.uicomponent.impl.viewholder.TaskHistoryViewHolder
import br.com.sailboat.uicomponent.model.SubheadUiModel
import br.com.sailboat.uicomponent.model.TaskHistoryUiModel
import br.com.sailboat.uicomponent.model.UiModel
import br.com.sailboat.uicomponent.model.UiModelType

internal class TaskHistoryAdapter(
    private val formatter: TaskHistoryDateTimeFormatter,
    private val callback: Callback,
) :
    ListAdapter<UiModel, RecyclerView.ViewHolder>(UiModelDiffUtilCallback()) {
    interface Callback : TaskHistoryViewHolder.Callback

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = when (viewType) {
        UiModelType.TASK_HISTORY.ordinal -> TaskHistoryViewHolder(parent, formatter, callback)
        UiModelType.SUBHEADER.ordinal -> SubheadViewHolder(parent)
        else -> EmptyViewHolder(parent)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = getItem(position)) {
            is TaskHistoryUiModel -> (holder as TaskHistoryViewHolder).bind(item)
            is SubheadUiModel -> (holder as SubheadViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).uiModelId
}
