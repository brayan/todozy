package br.com.sailboat.todozy.features.tasks.presentation.list.viewmodel

import br.com.sailboat.todozy.domain.model.TaskStatus

sealed class TaskListViewAction {
    object OnClickMenuSettings : TaskListViewAction()
    object OnClickMenuHistory : TaskListViewAction()
    object OnClickMenuAbout : TaskListViewAction()
    object OnClickNewTask : TaskListViewAction()
    object OnStart : TaskListViewAction()
    data class OnClickTask(val taskId: Long) : TaskListViewAction()
    data class OnInputSearchTerm(val term: String) : TaskListViewAction()
    data class OnSwipeTask(val position: Int, val status: TaskStatus) : TaskListViewAction()
}