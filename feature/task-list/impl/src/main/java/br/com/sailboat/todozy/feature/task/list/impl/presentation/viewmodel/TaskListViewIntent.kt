package br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel

import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.todozy.domain.model.TaskStatus

internal sealed class TaskListViewIntent {
    object OnStart : TaskListViewIntent()
    data class OnResume(val forceReload: Boolean = false) : TaskListViewIntent()
    object OnClickMenuAbout : TaskListViewIntent()
    object OnClickMenuSettings : TaskListViewIntent()
    object OnClickMenuHistory : TaskListViewIntent()
    object OnClickNewTask : TaskListViewIntent()
    data class OnClickTask(val taskId: Long) : TaskListViewIntent()
    data class OnMarkDoneForToday(val taskId: Long) : TaskListViewIntent()
    data class OnSubmitSearchTerm(val term: String) : TaskListViewIntent()
    data class OnSwipeTask(val taskId: Long, val status: TaskStatus) : TaskListViewIntent()
    data class OnClickUndoTask(val taskId: Long, val status: TaskStatus) : TaskListViewIntent()
    data class OnSelectProgressRange(val range: TaskProgressRange) : TaskListViewIntent()
}
