package br.com.sailboat.todozy.features.tasks.domain.usecase.tasks

import br.com.sailboat.todozy.features.tasks.domain.model.TaskStatus
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.GetAlarm
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.GetNextAlarm
import br.com.sailboat.todozy.features.tasks.domain.usecase.history.AddHistory
import kotlinx.coroutines.runBlocking

class MarkTask(private val getAlarm: GetAlarm,
               private val getTask: GetTask,
               private val getNextAlarm: GetNextAlarm,
               private val saveTask: SaveTask,
               private val disableTask: DisableTask,
               private val addHistory: AddHistory) {

    // TODO: Remove runBlocking
    suspend operator fun invoke(taskId: Long, status: TaskStatus) = runBlocking {
        val alarm = getAlarm(taskId)
        val task = getTask(taskId)

        if (alarm == null || alarm.isAlarmRepeating().not()) {
            disableTask(task)

        } else {
            task.alarm = getNextAlarm(alarm)
            saveTask(task)
        }

        addHistory(task, status)
    }

}