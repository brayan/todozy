package br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog

import androidx.lifecycle.MutableLiveData
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.DateFilterTaskHistorySelectableItem
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.TaskStatusSelectableItem

internal class TaskHistoryFilterViewState {
    val date = MutableLiveData<DateFilterTaskHistorySelectableItem>()
    val status = MutableLiveData<TaskStatusSelectableItem>()
}
