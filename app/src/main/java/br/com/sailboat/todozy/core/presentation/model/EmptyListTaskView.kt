package br.com.sailboat.todozy.core.presentation.model

import br.com.sailboat.todozy.uicomponent.model.UiModel
import br.com.sailboat.todozy.uicomponent.model.ViewType

data class EmptyListTaskView(
    override val index: Int = ViewType.EMPTY_LIST_TASK.ordinal
) : UiModel