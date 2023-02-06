package br.com.sailboat.todozy.feature.task.list.impl.presentation.viewmodel

internal sealed class TaskListViewAction {
    object CloseNotifications : TaskListViewAction()
    object NavigateToAbout : TaskListViewAction()
    object NavigateToHistory : TaskListViewAction()
    object NavigateToSettings : TaskListViewAction()
    object NavigateToTaskForm : TaskListViewAction()
    object ShowErrorLoadingTasks : TaskListViewAction()
    object ShowErrorCompletingTask : TaskListViewAction()
    data class NavigateToTaskDetails(val taskId: Long) : TaskListViewAction()
    data class UpdateRemovedTask(val position: Int) : TaskListViewAction()
}
