package br.com.sailboat.todozy.features.tasks.domain.usecase.tasks

import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.DeleteAlarm

class DisableTask(private val taskRepository: TaskRepository,
                  private val deleteAlarm: DeleteAlarm) {

    suspend operator fun invoke(task: Task) {
        taskRepository.disableTask(task)
        deleteAlarm(task)
    }

}