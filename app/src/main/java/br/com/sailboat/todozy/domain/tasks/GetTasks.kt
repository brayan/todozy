package br.com.sailboat.todozy.domain.tasks

import br.com.sailboat.todozy.domain.filter.TaskFilter
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.domain.repository.TaskRepository

class GetTasks(private val taskRepository: TaskRepository) {

    suspend operator fun invoke(filter: TaskFilter): List<Task> = with(taskRepository) {
        return when (filter.category) {
            TaskCategory.BEFORE_TODAY -> getBeforeTodayTasks(filter)
            TaskCategory.TODAY -> getTodayTasks(filter)
            TaskCategory.TOMORROW -> getTomorrowTasks(filter)
            TaskCategory.NEXT_DAYS -> getNextDaysTasks(filter)
        }
    }

}