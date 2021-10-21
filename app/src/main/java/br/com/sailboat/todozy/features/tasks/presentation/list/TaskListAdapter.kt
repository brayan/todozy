package br.com.sailboat.todozy.features.tasks.presentation.list

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.core.presentation.model.ItemView
import br.com.sailboat.todozy.core.presentation.model.SubheadView
import br.com.sailboat.todozy.core.presentation.model.TaskItemView
import br.com.sailboat.todozy.core.presentation.model.ViewType
import br.com.sailboat.todozy.core.presentation.viewholder.EmptyViewHolder
import br.com.sailboat.todozy.core.presentation.viewholder.SubheadViewHolder
import br.com.sailboat.todozy.core.presentation.viewholder.TaskViewHolder

class TaskListAdapter(private val callback: Callback) :
    ListAdapter<ItemView, RecyclerView.ViewHolder>(TaskListAdapterDiffUtilCallback()) {

    interface Callback : TaskViewHolder.Callback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ViewType.TASK.ordinal -> TaskViewHolder(parent, callback)
        ViewType.SUBHEADER.ordinal -> SubheadViewHolder(parent)
        else -> EmptyViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (item.viewType) {
            ViewType.TASK.ordinal -> (holder as TaskViewHolder).bind(item as TaskItemView)
            ViewType.SUBHEADER.ordinal -> (holder as SubheadViewHolder).bind(item as SubheadView)
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).viewType

    private class TaskListAdapterDiffUtilCallback : DiffUtil.ItemCallback<ItemView>() {
        override fun areItemsTheSame(
            oldItem: ItemView,
            newItem: ItemView,
        ) = oldItem.viewType == newItem.viewType

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ItemView,
            newItem: ItemView,
        ) = oldItem == newItem
    }
}