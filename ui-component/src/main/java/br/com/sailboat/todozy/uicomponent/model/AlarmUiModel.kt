package br.com.sailboat.todozy.uicomponent.model

data class AlarmUiModel(
    val date: String,
    val time: String,
    val description: String,
    val isCustom: Boolean,
    val shouldRepeat: Boolean,
    val customDays: String?,
    override val index: Int = UiModelType.ALARM.ordinal,
) : UiModel