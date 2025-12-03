package br.com.sailboat.uicomponent.impl.viewholder

import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.uicomponent.impl.theme.TodozyTheme
import br.com.sailboat.uicomponent.impl.task.TaskItem
import br.com.sailboat.uicomponent.model.TaskUiModel

class TaskViewHolder(
    parent: ViewGroup,
    private val callback: Callback,
) : RecyclerView.ViewHolder(
        ComposeView(parent.context).apply {
            layoutParams =
                RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT,
                )
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        },
    ) {
    interface Callback {
        fun onClickTask(taskId: Long)
        fun onClickUndo(
            taskId: Long,
            status: TaskStatus,
        )
    }

    private val composeView get() = itemView as ComposeView

    fun bind(item: TaskUiModel) {
        composeView.setContent {
            TodozyTheme {
                Surface {
                    TaskItem(
                        task = item,
                        onClick = callback::onClickTask,
                        onUndoClick = callback::onClickUndo,
                    )
                }
            }
        }
    }
}
