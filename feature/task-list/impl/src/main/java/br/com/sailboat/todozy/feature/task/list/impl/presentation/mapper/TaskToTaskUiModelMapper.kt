package br.com.sailboat.todozy.feature.task.list.impl.presentation.mapper

import android.content.Context
import androidx.core.content.ContextCompat
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.utility.kotlin.extension.isBeforeNow
import br.com.sailboat.todozy.utility.kotlin.extension.isToday
import br.com.sailboat.todozy.utility.kotlin.extension.isTomorrow
import br.com.sailboat.todozy.utility.kotlin.extension.isTrue
import br.com.sailboat.uicomponent.model.TaskUiModel
import br.com.sailboat.uicomponent.impl.R as UiR
import java.util.Calendar

internal class TaskToTaskUiModelMapper(private val context: Context) {

    fun map(tasks: List<Task>): List<TaskUiModel> = tasks.map { map(it) }

    private fun map(task: Task): TaskUiModel {
        return TaskUiModel(
            taskId = task.id,
            taskName = task.name,
            alarm = task.alarm?.dateTime,
            alarmColor = getAlarmColor(task.alarm?.dateTime)
        )
    }

    private fun getAlarmColor(alarm: Calendar?): Int {
        val colorRes = when {
            alarm?.isBeforeNow().isTrue() -> UiR.color.md_grey_500
            alarm?.isToday().isTrue() -> UiR.color.md_teal_300
            alarm?.isTomorrow().isTrue() -> UiR.color.md_blue_300
            else -> UiR.color.md_blue_500
        }

        return ContextCompat.getColor(context, colorRes)
    }
}
