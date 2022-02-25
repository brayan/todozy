package br.com.sailboat.todozy.features.tasks.domain.model

import br.com.sailboat.todozy.utility.kotlin.model.BaseFilter

data class TaskFilter(var category: TaskCategory) : BaseFilter()