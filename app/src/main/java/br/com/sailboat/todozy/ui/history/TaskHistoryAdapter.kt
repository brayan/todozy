package br.com.sailboat.todozy.ui.history

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.ui.model.*
import br.com.sailboat.todozy.ui.model.view_holder.SubheaderViewHolder
import br.com.sailboat.todozy.ui.model.view_holder.TaskHistoryViewHolder
import br.com.sailboat.todozy.ui.model.view_holder.TaskViewHolder

class TaskHistoryAdapter(private val callback: Callback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface Callback : TaskViewHolder.Callback {
        val history: List<ItemView>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ViewType.TASK_HISTORY.ordinal -> TaskViewHolder(parent, callback)
        ViewType.SUBHEADER.ordinal -> SubheaderViewHolder(parent)
        else -> throw RuntimeException("ViewHolder not found")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = callback.history[position]

        when (item.viewType) {
            ViewType.TASK_HISTORY.ordinal -> (holder as TaskHistoryViewHolder).bind(item as TaskHistoryView)
            ViewType.SUBHEADER.ordinal -> (holder as SubheaderViewHolder).bind(item as SubheadView)
            else -> throw RuntimeException("ViewHolder not found")
        }
    }

    override fun getItemCount() = callback.history.size

    override fun getItemViewType(position: Int) = callback.history[position].viewType

}