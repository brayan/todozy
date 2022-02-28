package br.com.sailboat.todozy.features.tasks.presentation.details

import android.content.Context
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.feature.alarm.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.feature.alarm.presentation.mapper.AlarmToAlarmUiModelMapper
import br.com.sailboat.todozy.uicomponent.model.*

// TODO: Add unit tests
class GetTaskDetailsView(
    private val context: Context,
    private val getTaskUseCase: GetTaskUseCase,
    private val alarmToAlarmUiModelMapper: AlarmToAlarmUiModelMapper,
) : GetTaskDetailsViewUseCase {

    override suspend operator fun invoke(taskId: Long): List<UiModel> {
        val itemViews = mutableListOf<UiModel>()

        val task = getTaskUseCase(taskId)

        addTitle(itemViews, task)
        task.alarm?.run { addAlarm(context, this, itemViews) }
        task.notes?.takeIf { it.isNotBlank() }?.run { addNotes(context, itemViews, this) }

        return itemViews
    }

    private fun addTitle(uiModels: MutableList<UiModel>, task: Task) {
        val item = TitleUiModel(task.name)
        uiModels.add(item)
    }

    private fun addNotes(context: Context, uiModels: MutableList<UiModel>, notes: String) {
        uiModels.add(getLabelValueNotes(context, notes))
    }

    private fun addAlarm(context: Context, alarm: Alarm, uiModels: MutableList<UiModel>) {
        val item = LabelUiModel(context.getString(R.string.alarm), UiModelType.LABEL.ordinal)
        uiModels.add(item)

        val alarmView = alarmToAlarmUiModelMapper.map(alarm)
        uiModels.add(alarmView)
    }

    private fun getLabelValueNotes(context: Context, notes: String): LabelValueUiModel {
        return LabelValueUiModel(
            label = context.getString(R.string.notes),
            value = notes
        )
    }

}