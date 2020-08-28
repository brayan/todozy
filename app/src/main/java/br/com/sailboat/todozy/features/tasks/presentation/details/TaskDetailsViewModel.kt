package br.com.sailboat.todozy.features.tasks.presentation.details

import br.com.sailboat.todozy.core.presentation.model.ItemView
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.TaskMetrics

class TaskDetailsViewModel {

    lateinit var taskMetrics: TaskMetrics
    val details = mutableListOf<ItemView>()
    var taskId: Long = 0
    var alarm: Alarm? = null

}