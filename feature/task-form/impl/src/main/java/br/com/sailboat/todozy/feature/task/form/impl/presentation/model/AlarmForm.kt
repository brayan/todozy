package br.com.sailboat.todozy.feature.task.form.impl.presentation.model

import java.util.Calendar

data class AlarmForm(
    val alarm: Calendar?,
    val date: String?,
    val time: String?,
    val repeatType: String?,
    val alarmCustomDays: String?,
    val animate: Boolean,
    val visible: Boolean,
)
