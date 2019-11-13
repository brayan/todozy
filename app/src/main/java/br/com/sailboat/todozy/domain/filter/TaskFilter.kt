package br.com.sailboat.todozy.domain.filter

import br.com.sailboat.todozy.domain.model.TaskCategory

data class TaskFilter(var category: TaskCategory) : BaseFilter()