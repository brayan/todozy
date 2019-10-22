package br.com.sailboat.todozy.ui.task.details

import android.content.Context
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.tasks.GetTask
import br.com.sailboat.todozy.ui.helper.RepeatTypeView
import br.com.sailboat.todozy.ui.model.*
import kotlinx.coroutines.coroutineScope

class GetTaskDetailsView(private val context: Context, private val getTask: GetTask) {

    suspend operator fun invoke(taskId: Long): List<ItemView> = coroutineScope {
        val itemViews = ArrayList<ItemView>()

        val task = getTask(taskId)

        addTitle(itemViews, task)
        task.alarm?.run { addAlarm(context, this, itemViews) }
        task.notes?.run { addNotes(context, itemViews, this) }

        itemViews
    }

    private fun addTitle(itemViews: ArrayList<ItemView>, task: Task) {
        val item = TitleItemView(task.name)
        itemViews.add(item)
    }

    private fun addNotes(context: Context, itemViews: ArrayList<ItemView>, notes: String) {
        itemViews.add(getLabelValueNotes(context, notes))
    }

    private fun addAlarm(context: Context, alarm: Alarm, itemViews: ArrayList<ItemView>) {
        val item = LabelItemView(context.getString(R.string.alarm), ViewType.LABEL.ordinal)
        itemViews.add(item)

        val alarmView = AlarmView(dateTime = alarm.dateTime,
                customDays = alarm.customDays,
                repeatType = RepeatTypeView.getFromRepeatType(alarm.repeatType)!!)
        itemViews.add(alarmView)

    }

    private fun getLabelValueNotes(context: Context, notes: String): LabelValueItemView {
        return LabelValueItemView(label = context.getString(R.string.notes),
                value = notes)
    }

}