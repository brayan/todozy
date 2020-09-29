package br.com.sailboat.todozy.features.tasks.domain.usecase

import br.com.sailboat.todozy.features.tasks.domain.model.TaskStatus
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.GetNextAlarm
import br.com.sailboat.todozy.features.tasks.domain.usecase.history.AddHistory

class CompleteTask(private val getTask: GetTask,
                   private val getNextAlarm: GetNextAlarm,
                   private val saveTask: SaveTask,
                   private val disableTask: DisableTask,
                   private val addHistory: AddHistory) {

    suspend operator fun invoke(taskId: Long, status: TaskStatus) {
        val task = getTask(taskId)

        task.alarm?.takeIf { it.isAlarmRepeating() }?.run {

            task.alarm = getNextAlarm(this)
            saveTask(task)

        } ?: disableTask(task)

        addHistory(task, status)
    }

}