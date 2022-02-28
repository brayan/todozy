package br.com.sailboat.todozy.features.tasks.presentation.list

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.uicomponent.model.TaskUiModel
import br.com.sailboat.todozy.core.presentation.viewholder.EmptyViewHolder
import br.com.sailboat.todozy.core.presentation.viewholder.SubheadViewHolder
import br.com.sailboat.todozy.uicomponent.viewholder.TaskViewHolder
import br.com.sailboat.todozy.uicomponent.model.SubheadView
import br.com.sailboat.todozy.uicomponent.model.UiModel
import br.com.sailboat.todozy.uicomponent.model.UiModelType

class TaskListAdapter(private val callback: Callback) :
    ListAdapter<UiModel, RecyclerView.ViewHolder>(TaskListAdapterDiffUtilCallback()) {

    interface Callback : TaskViewHolder.Callback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        UiModelType.TASK.ordinal -> TaskViewHolder(parent, callback)
        UiModelType.SUBHEADER.ordinal -> SubheadViewHolder(parent)
        else -> EmptyViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (item.index) {
            UiModelType.TASK.ordinal -> (holder as TaskViewHolder).bind(item as TaskUiModel)
            UiModelType.SUBHEADER.ordinal -> (holder as SubheadViewHolder).bind(item as SubheadView)
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).index

    private class TaskListAdapterDiffUtilCallback : DiffUtil.ItemCallback<UiModel>() {
        override fun areItemsTheSame(
            oldItem: UiModel,
            newItem: UiModel,
        ) = oldItem.index == newItem.index

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: UiModel,
            newItem: UiModel,
        ) = oldItem == newItem
    }
}