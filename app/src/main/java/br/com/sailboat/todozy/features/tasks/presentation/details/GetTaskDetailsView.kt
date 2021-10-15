package br.com.sailboat.todozy.features.tasks.presentation.details

import android.content.Context
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.helper.RepeatTypeView
import br.com.sailboat.todozy.core.presentation.model.*
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.usecase.GetTaskUseCase

class GetTaskDetailsView(
    private val context: Context,
    private val getTaskUseCase: GetTaskUseCase,
) {

    suspend operator fun invoke(taskId: Long): List<ItemView> {
        val itemViews = mutableListOf<ItemView>()

        val task = getTaskUseCase(taskId)

        addTitle(itemViews, task)
        task.alarm?.run { addAlarm(context, this, itemViews) }
        task.notes?.takeIf { it.isNotBlank() }?.run { addNotes(context, itemViews, this) }

        return itemViews
    }

    private fun addTitle(itemViews: MutableList<ItemView>, task: Task) {
        val item = TitleItemView(task.name)
        itemViews.add(item)
    }

    private fun addNotes(context: Context, itemViews: MutableList<ItemView>, notes: String) {
        itemViews.add(getLabelValueNotes(context, notes))
    }

    private fun addAlarm(context: Context, alarm: Alarm, itemViews: MutableList<ItemView>) {
        val item = LabelItemView(context.getString(R.string.alarm), ViewType.LABEL.ordinal)
        itemViews.add(item)

        val alarmView = AlarmView(
            dateTime = alarm.dateTime,
            customDays = alarm.customDays,
            repeatType = RepeatTypeView.getFromRepeatType(alarm.repeatType)
        )
        itemViews.add(alarmView)

    }

    private fun getLabelValueNotes(context: Context, notes: String): LabelValueItemView {
        return LabelValueItemView(
            label = context.getString(R.string.notes),
            value = notes
        )
    }

}