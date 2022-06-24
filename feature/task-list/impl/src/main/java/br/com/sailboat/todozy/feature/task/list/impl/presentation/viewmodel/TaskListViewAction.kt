package br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel

import br.com.sailboat.todozy.domain.model.TaskStatus

internal sealed class TaskListViewAction {
    object OnStart : TaskListViewAction()
    object OnClickMenuAbout : TaskListViewAction()
    object OnClickMenuSettings : TaskListViewAction()
    object OnClickMenuHistory : TaskListViewAction()
    object OnClickNewTask : TaskListViewAction()
    data class OnClickTask(val taskId: Long) : TaskListViewAction()
    data class OnSubmitSearchTerm(val term: String) : TaskListViewAction()
    data class OnSwipeTask(val position: Int, val status: TaskStatus) : TaskListViewAction()
}
