package br.com.sailboat.todozy.feature.task.details.impl.presentation

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.uicomponent.model.UiModel

class TaskDetailsViewModel {

    lateinit var taskMetrics: TaskMetrics
    val details = mutableListOf<UiModel>()
    var taskId: Long = 0
    var alarm: Alarm? = null

}