package br.com.sailboat.todozy.feature.task.list.impl.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.uicomponent.impl.helper.UiModelDiffUtilCallback
import br.com.sailboat.uicomponent.impl.viewholder.EmptyViewHolder
import br.com.sailboat.uicomponent.impl.viewholder.SubheadViewHolder
import br.com.sailboat.uicomponent.impl.viewholder.TaskViewHolder
import br.com.sailboat.uicomponent.model.SubheadUiModel
import br.com.sailboat.uicomponent.model.TaskUiModel
import br.com.sailboat.uicomponent.model.UiModel
import br.com.sailboat.uicomponent.model.UiModelType

internal class TaskListAdapter(private val callback: Callback) :
    ListAdapter<UiModel, RecyclerView.ViewHolder>(UiModelDiffUtilCallback()) {

    interface Callback : TaskViewHolder.Callback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        UiModelType.TASK.ordinal -> TaskViewHolder(parent, callback)
        UiModelType.SUBHEADER.ordinal -> SubheadViewHolder(parent)
        else -> EmptyViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is TaskUiModel -> (holder as TaskViewHolder).bind(item)
            is SubheadUiModel -> (holder as SubheadViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).uiModelId
}
