package br.com.sailboat.todozy.domain.model

import br.com.sailboat.todozy.utility.kotlin.model.Filter

data class TaskFilter(
    override val text: String? = null,
    val category: TaskCategory,
) : Filter
