package br.com.sailboat.uicomponent.model

data class AlarmUiModel(
    val date: String,
    val time: String,
    val description: String,
    val isCustom: Boolean,
    val shouldRepeat: Boolean,
    val customDays: String?,
    override val uiModelId: Int = UiModelType.ALARM.ordinal,
) : UiModel
