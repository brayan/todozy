package br.com.sailboat.todozy.domain.tasks

import br.com.sailboat.todozy.domain.filter.TaskFilter
import br.com.sailboat.todozy.domain.model.TaskType
import br.com.sailboat.todozy.domain.repository.TaskRepository

class GetTasks(private val taskRepository: TaskRepository) {

    suspend operator fun invoke(filter: TaskFilter) = when (filter.type) {
        TaskType.BEFORE_TODAY -> this.taskRepository.getBeforeTodayTasks(filter)
        TaskType.TODAY -> this.taskRepository.getTodayTasks(filter)
        TaskType.TOMORROW -> this.taskRepository.getTomorrowTasks(filter)
        TaskType.NEXT_DAYS -> this.taskRepository.getNextDaysTasks(filter)
    }

}