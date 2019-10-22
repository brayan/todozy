package br.com.sailboat.todozy.ui.task.details

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.ui.model.ItemView

class TaskDetailsViewModel {

    lateinit var taskMetrics: TaskMetrics
    val details = mutableListOf<ItemView>()
    var taskId: Long = 0
    var alarm: Alarm? = null

}