package br.com.sailboat.todozy.domain.model

import br.com.sailboat.todozy.utility.kotlin.model.BaseFilter

data class TaskFilter(var category: TaskCategory) : BaseFilter()