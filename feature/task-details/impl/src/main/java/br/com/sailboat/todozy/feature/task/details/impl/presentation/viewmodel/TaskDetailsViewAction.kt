package br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel

internal sealed class TaskDetailsViewAction {
    object ConfirmDeleteTask : TaskDetailsViewAction()
    object ShowErrorLoadingTaskDetails : TaskDetailsViewAction()
    data class NavigateToTaskForm(val taskId: Long) : TaskDetailsViewAction()
    data class CloseTaskDetails(val success: Boolean) : TaskDetailsViewAction()
}
