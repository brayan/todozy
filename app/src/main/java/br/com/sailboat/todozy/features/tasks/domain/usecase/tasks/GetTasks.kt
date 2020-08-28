package br.com.sailboat.todozy.features.tasks.domain.usecase.tasks

import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.model.TaskCategory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskFilter
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository

class GetTasks(private val taskRepository: TaskRepository) {

    suspend operator fun invoke(filter: TaskFilter): List<Task> = with(taskRepository) {
        return when (filter.category) {
            TaskCategory.BEFORE_TODAY -> getBeforeTodayTasks(filter)
            TaskCategory.BEFORE_NOW -> getBeforeNowTasks()
            TaskCategory.TODAY -> getTodayTasks(filter)
            TaskCategory.TOMORROW -> getTomorrowTasks(filter)
            TaskCategory.NEXT_DAYS -> getNextDaysTasks(filter)

        }
    }

}