package br.com.sailboat.todozy.feature.task.details.impl.presentation.viewmodel

import br.com.sailboat.todozy.domain.model.TaskProgressRange

internal sealed class TaskDetailsViewIntent {
    object OnClickMenuDelete : TaskDetailsViewIntent()
    object OnClickEditTask : TaskDetailsViewIntent()
    object OnClickConfirmDeleteTask : TaskDetailsViewIntent()
    object OnReturnToDetails : TaskDetailsViewIntent()
    data class OnSelectProgressRange(val range: TaskProgressRange) : TaskDetailsViewIntent()
    data class OnStart(val taskId: Long) : TaskDetailsViewIntent()
}
