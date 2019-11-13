package br.com.sailboat.todozy.domain.tasks

import br.com.sailboat.todozy.domain.filter.TaskFilter
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskCategory.*
import br.com.sailboat.todozy.domain.repository.TaskRepository

class GetTasks(private val taskRepository: TaskRepository) {

    suspend operator fun invoke(filter: TaskFilter): List<Task> = with(taskRepository) {
        return when (filter.category) {
            BEFORE_TODAY -> getBeforeTodayTasks(filter)
            TODAY -> getTodayTasks(filter)
            TOMORROW -> getTomorrowTasks(filter)
            NEXT_DAYS -> getNextDaysTasks(filter)
        }
    }

}