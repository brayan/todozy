package br.com.sailboat.todozy.feature.task.history.impl.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.uicomponent.model.SubheadUiModel
import br.com.sailboat.todozy.uicomponent.model.TaskHistoryUiModel
import br.com.sailboat.todozy.uicomponent.model.UiModel
import br.com.sailboat.todozy.uicomponent.model.UiModelType
import br.com.sailboat.todozy.uicomponent.viewholder.SubheadViewHolder
import br.com.sailboat.todozy.uicomponent.viewholder.TaskHistoryViewHolder

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
            UiModelType.TASK_HISTORY.ordinal -> (holder as TaskHistoryViewHolder).bind(item as TaskHistoryUiModel)
            UiModelType.SUBHEADER.ordinal -> (holder as SubheadViewHolder).bind(item as SubheadUiModel)
            else -> throw RuntimeException("ViewHolder not found")
        }
    }

    override fun getItemCount() = callback.history.size

    override fun getItemViewType(position: Int) = callback.history[position].index

}