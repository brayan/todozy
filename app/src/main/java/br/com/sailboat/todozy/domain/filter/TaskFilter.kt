package br.com.sailboat.todozy.domain.filter

import br.com.sailboat.todozy.domain.model.TaskType

data class TaskFilter(var type: TaskType) : BaseFilter()