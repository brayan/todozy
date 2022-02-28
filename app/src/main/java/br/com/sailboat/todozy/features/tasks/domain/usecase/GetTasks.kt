package br.com.sailboat.todozy.features.tasks.domain.usecase

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.domain.model.TaskFilter
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository

class GetTasks(private val taskRepository: TaskRepository) : GetTasksUseCase {

    override suspend operator fun invoke(filter: TaskFilter): List<Task> = with(taskRepository) {
        return when (filter.category) {
            TaskCategory.BEFORE_TODAY -> getBeforeTodayTasks(filter)
            TaskCategory.BEFORE_NOW -> getBeforeNowTasks()
            TaskCategory.TODAY -> getTodayTasks(filter)
            TaskCategory.TOMORROW -> getTomorrowTasks(filter)
            TaskCategory.NEXT_DAYS -> getNextDaysTasks(filter)
            TaskCategory.WITH_ALARMS -> getTasksWithAlarms()
        }
    }

}