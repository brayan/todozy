package br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel

import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.todozy.domain.model.TaskStatus

internal sealed class TaskListViewIntent {
    object OnStart : TaskListViewIntent()
    object OnClickMenuAbout : TaskListViewIntent()
    object OnClickMenuSettings : TaskListViewIntent()
    object OnClickMenuHistory : TaskListViewIntent()
    object OnClickNewTask : TaskListViewIntent()
    data class OnClickTask(val taskId: Long) : TaskListViewIntent()
    data class OnSubmitSearchTerm(val term: String) : TaskListViewIntent()
    data class OnSwipeTask(val position: Int, val status: TaskStatus) : TaskListViewIntent()
    data class OnSelectProgressRange(val range: TaskProgressRange) : TaskListViewIntent()
}
