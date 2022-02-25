package br.com.sailboat.todozy.features.tasks.presentation.details

import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.TaskMetrics
import br.com.sailboat.todozy.uicomponent.model.UiModel

class TaskDetailsViewModel {

    lateinit var taskMetrics: TaskMetrics
    val details = mutableListOf<UiModel>()
    var taskId: Long = 0
    var alarm: Alarm? = null

}