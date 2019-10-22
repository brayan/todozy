package br.com.sailboat.todozy.ui.task.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.ui.model.ItemView
import br.com.sailboat.todozy.ui.model.SubheadView
import br.com.sailboat.todozy.ui.model.TaskItemView
import br.com.sailboat.todozy.ui.model.ViewType
import br.com.sailboat.todozy.ui.model.view_holder.SubheaderViewHolder
import br.com.sailboat.todozy.ui.model.view_holder.TaskViewHolder

class TaskListAdapter(private val callback: Callback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface Callback : TaskViewHolder.Callback {
        val taskViewList: List<ItemView>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ViewType.TASK.ordinal -> TaskViewHolder(parent, callback)
        ViewType.SUBHEADER.ordinal -> SubheaderViewHolder(parent)
        else -> throw RuntimeException("ViewHolder not found")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = callback.taskViewList[position]

        when (item.viewType) {
            ViewType.TASK.ordinal -> (holder as TaskViewHolder).bind(item as TaskItemView)
            ViewType.SUBHEADER.ordinal -> (holder as SubheaderViewHolder).bind(item as SubheadView)
            else -> throw RuntimeException("ViewHolder not found")
        }
    }

    override fun getItemCount() = callback.taskViewList.size

    override fun getItemViewType(position: Int) = callback.taskViewList[position].viewType

}