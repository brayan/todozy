package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository

class DeleteAlarm(private val alarmRepository: AlarmRepository) {

    suspend operator fun invoke(task: Task) {
        alarmRepository.deleteAlarmByTask(task)
    }

}