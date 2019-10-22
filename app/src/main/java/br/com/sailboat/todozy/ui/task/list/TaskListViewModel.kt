package br.com.sailboat.todozy.ui.task.list

import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.filter.TaskFilter
import br.com.sailboat.todozy.domain.model.TaskType
import br.com.sailboat.todozy.ui.model.ItemView

class TaskListViewModel {

    val tasksView = mutableListOf<ItemView>()
    var taskMetrics: TaskMetrics? = null
    var filter = TaskFilter(TaskType.TODAY)

}