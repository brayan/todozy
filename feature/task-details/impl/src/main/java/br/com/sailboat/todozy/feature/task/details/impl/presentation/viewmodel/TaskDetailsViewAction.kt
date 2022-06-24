package br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel

internal sealed class TaskDetailsViewAction {
    data class OnStart(val taskId: Long) : TaskDetailsViewAction()
    object OnClickMenuDelete : TaskDetailsViewAction()
    object OnClickEditTask : TaskDetailsViewAction()
    object OnClickConfirmDeleteTask : TaskDetailsViewAction()
    object OnReturnToDetails : TaskDetailsViewAction()
}
