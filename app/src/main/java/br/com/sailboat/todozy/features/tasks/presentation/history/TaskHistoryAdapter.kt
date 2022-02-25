package br.com.sailboat.todozy.features.tasks.presentation.history

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.core.presentation.model.TaskHistoryView
import br.com.sailboat.todozy.core.presentation.viewholder.SubheadViewHolder
import br.com.sailboat.todozy.core.presentation.viewholder.TaskHistoryViewHolder
import br.com.sailboat.todozy.uicomponent.model.SubheadView
import br.com.sailboat.todozy.uicomponent.model.UiModel
import br.com.sailboat.todozy.uicomponent.model.UiModelType

class TaskHistoryAdapter(private val callback: Callback) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface Callback : TaskHistoryViewHolder.Callback {
        val history: List<UiModel>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        UiModelType.TASK_HISTORY.ordinal -> TaskHistoryViewHolder(parent, callback)
        UiModelType.SUBHEADER.ordinal -> SubheadViewHolder(parent)
        else -> throw RuntimeException("ViewHolder not found")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = callback.history[position]

        when (item.index) {
            UiModelType.TASK_HISTORY.ordinal -> (holder as TaskHistoryViewHolder).bind(item as TaskHistoryView)
            UiModelType.SUBHEADER.ordinal -> (holder as SubheadViewHolder).bind(item as SubheadView)
            else -> throw RuntimeException("ViewHolder not found")
        }
    }

    override fun getItemCount() = callback.history.size

    override fun getItemViewType(position: Int) = callback.history[position].index

}