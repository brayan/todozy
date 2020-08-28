package br.com.sailboat.todozy.features.tasks.domain.model

import br.com.sailboat.todozy.core.base.BaseFilter

data class TaskFilter(var category: TaskCategory) : BaseFilter()