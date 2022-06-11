package br.com.sailboat.todozy.feature.alarm.presentation.mapper

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.uicomponent.model.AlarmUiModel

interface AlarmToAlarmUiModelMapper {
    fun map(alarm: Alarm): AlarmUiModel
}