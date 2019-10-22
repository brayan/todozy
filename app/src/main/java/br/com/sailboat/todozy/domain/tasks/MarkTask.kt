package br.com.sailboat.todozy.domain.tasks

import br.com.sailboat.todozy.domain.alarm.GetAlarm
import br.com.sailboat.todozy.domain.alarm.SetNextValidAlarm
import br.com.sailboat.todozy.domain.alarm.UpdateAlarm
import br.com.sailboat.todozy.domain.history.AddHistory
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.TaskStatus

class MarkTask(private val getAlarm: GetAlarm,
               private val getTask: GetTask,
               private val updateAlarm: UpdateAlarm,
               private val disableTask: DisableTask,
               private val setNextValidAlarm: SetNextValidAlarm,
               private val addHistory: AddHistory) {


    suspend operator fun invoke(taskId: Long, status: TaskStatus) {
        val alarm = this.getAlarm(taskId)
        val task = getTask(taskId)

        if (alarm == null) {
            disableTask(task)

        } else {

            if (RepeatType.isAlarmRepeating(alarm)) {
                val nextValidAlarm = setNextValidAlarm(alarm, task)
                updateAlarm(nextValidAlarm)
            } else {
                disableTask(task)
            }
        }

        addHistory(task, status)
    }

}