package br.com.sailboat.todozy.feature.task.details.impl.presentation.factory

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.feature.alarm.presentation.mapper.AlarmToAlarmUiModelMapper
import br.com.sailboat.todozy.feature.task.details.impl.R
import br.com.sailboat.todozy.utility.android.string.StringProvider
import br.com.sailboat.uicomponent.model.LabelUiModel
import br.com.sailboat.uicomponent.model.LabelValueUiModel
import br.com.sailboat.uicomponent.model.TitleUiModel
import br.com.sailboat.uicomponent.model.UiModel
import br.com.sailboat.uicomponent.model.UiModelType

internal class TaskDetailsUiModelFactory(
    private val stringProvider: StringProvider,
    private val alarmToAlarmUiModelMapper: AlarmToAlarmUiModelMapper,
) {

    fun create(task: Task): List<UiModel> {
        val uiModelList = mutableListOf<UiModel>()

        addTitle(uiModelList, task)
        task.alarm?.run { addAlarm(this, uiModelList) }
        task.notes?.takeIf { it.isNotBlank() }?.run {
            uiModelList.add(getLabelValueNotes(this))
        }

        return uiModelList
    }

    private fun addTitle(uiModelList: MutableList<UiModel>, task: Task) {
        val item = TitleUiModel(task.name)
        uiModelList.add(item)
    }

    private fun addAlarm(alarm: Alarm, uiModelList: MutableList<UiModel>) {
        val item = LabelUiModel(stringProvider.getString(R.string.alarm), UiModelType.LABEL.ordinal)
        uiModelList.add(item)

        val alarmView = alarmToAlarmUiModelMapper.map(alarm)
        uiModelList.add(alarmView)
    }

    private fun getLabelValueNotes(notes: String): LabelValueUiModel {
        return LabelValueUiModel(
            label = stringProvider.getString(R.string.notes),
            value = notes,
        )
    }
}
