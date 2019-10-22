package br.com.sailboat.todozy.domain.tasks

import br.com.sailboat.todozy.domain.alarm.DeleteAlarm
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.repository.TaskRepository

class DisableTask(private val taskRepository: TaskRepository,
                  private val deleteAlarm: DeleteAlarm) {

    suspend operator fun invoke(task: Task) {
        taskRepository.disableTask(task)
        deleteAlarm(task)
    }

}