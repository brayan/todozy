package br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog

import br.com.sailboat.uicomponent.impl.dialog.selectable.model.DateFilterTaskHistorySelectableItem
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.TaskStatusSelectableItem

internal sealed class TaskHistoryFilterViewAction {
    data class OnStart(
        val date: DateFilterTaskHistorySelectableItem?,
        val status: TaskStatusSelectableItem?,
    ) : TaskHistoryFilterViewAction()
}
