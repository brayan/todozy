package br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog

import androidx.lifecycle.MutableLiveData
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.DateFilterTaskHistorySelectableItem
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.TaskStatusSelectableItem

internal class TaskHistoryFilterViewState {
    var date = MutableLiveData<DateFilterTaskHistorySelectableItem>()
    var status = MutableLiveData<TaskStatusSelectableItem>()
}
