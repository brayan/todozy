package br.com.sailboat.todozy.core.presentation.model

data class EmptyListTaskView(
        override val viewType: Int = ViewType.EMPTY_LIST_TASK.ordinal) : ItemView