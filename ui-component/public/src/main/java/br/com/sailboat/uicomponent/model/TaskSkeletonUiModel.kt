package br.com.sailboat.uicomponent.model

data class TaskSkeletonUiModel(
    val placeholderId: Long,
    override val uiModelId: Int = UiModelType.TASK_SKELETON.ordinal,
) : UiModel
