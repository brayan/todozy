package br.com.sailboat.todozy.feature.task.list.impl.presentation

import android.content.Context
import androidx.core.content.ContextCompat
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.uicomponent.R
import br.com.sailboat.todozy.uicomponent.model.TaskUiModel
import br.com.sailboat.todozy.utility.kotlin.extension.isBeforeNow
import br.com.sailboat.todozy.utility.kotlin.extension.isToday
import br.com.sailboat.todozy.utility.kotlin.extension.isTomorrow
import br.com.sailboat.todozy.utility.kotlin.extension.isTrue
import java.util.*

class TaskToTaskUiModelMapper(private val context: Context) {

    fun map(task: Task): TaskUiModel {

        return TaskUiModel(
            taskId = task.id,
            taskName = task.name,
            alarm = task.alarm?.dateTime,
            alarmColor = getAlarmColor(task.alarm?.dateTime)
        )
    }

    private fun getAlarmColor(alarm: Calendar?): Int {
        val colorRes = when {
            alarm?.isBeforeNow().isTrue() -> R.color.md_grey_500
            alarm?.isToday().isTrue() -> R.color.md_teal_300
            alarm?.isTomorrow().isTrue() -> R.color.md_blue_300
            else -> R.color.md_blue_500
        }

        return ContextCompat.getColor(context, colorRes)
    }

}