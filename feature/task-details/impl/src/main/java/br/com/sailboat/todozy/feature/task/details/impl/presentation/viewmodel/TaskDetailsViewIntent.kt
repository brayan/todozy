package br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel

internal sealed class TaskDetailsViewIntent {
    object OnClickMenuDelete : TaskDetailsViewIntent()
    object OnClickEditTask : TaskDetailsViewIntent()
    object OnClickConfirmDeleteTask : TaskDetailsViewIntent()
    object OnReturnToDetails : TaskDetailsViewIntent()
    data class OnStart(val taskId: Long) : TaskDetailsViewIntent()
}
