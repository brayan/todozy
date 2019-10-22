package br.com.sailboat.todozy.data.mapper

import br.com.sailboat.todozy.data.model.AlarmData
import br.com.sailboat.todozy.data.model.TaskData
import br.com.sailboat.todozy.data.model.TaskHistoryData
import br.com.sailboat.todozy.domain.helper.EntityHelper
import br.com.sailboat.todozy.domain.helper.toDateTimeCalendar
import br.com.sailboat.todozy.domain.helper.toDateTimeString
import br.com.sailboat.todozy.domain.model.*
import java.util.*

fun TaskData.mapToTask(alarm: Alarm?) = Task(id = id,
        name = name ?: "",
        notes = notes,
        alarm = alarm)

fun Task.mapToTaskData() = TaskData(id = id, name = name, notes = notes)

fun TaskHistoryData.mapToTaskHistory() = TaskHistory(taskId = taskId, status = TaskStatus.indexOf(status))

fun List<TaskHistoryData>.mapToTaskHistoryList() = map { it.mapToTaskHistory() }

fun AlarmData.mapToAlarm() = Alarm(
        dateTime = nextAlarmDate!!.toDateTimeCalendar(),
        repeatType = RepeatType.indexOf(repeatType),
        customDays = days)

fun Alarm.mapToAlarmData(taskId: Long) = AlarmData(id = EntityHelper.NO_ID,
        taskId = taskId,
        repeatType = repeatType.ordinal,
        nextAlarmDate = dateTime.toDateTimeString(),
        insertingDate = Calendar.getInstance().toDateTimeString(),
        days = customDays)

