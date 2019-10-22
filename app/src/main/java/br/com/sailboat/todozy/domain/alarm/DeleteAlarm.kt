package br.com.sailboat.todozy.domain.alarm

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.repository.AlarmRepository
import br.com.sailboat.todozy.domain.tasks.GetTask

class DeleteAlarm(private val alarmRepository: AlarmRepository) {

    suspend operator fun invoke(task: Task) {
        alarmRepository.deleteAlarmByTask(task)
    }

}