package br.com.sailboat.todozy.features.tasks.domain

import br.com.sailboat.todozy.domain.model.TaskFieldsConditions

class TaskFieldsException(val conditions: List<TaskFieldsConditions>) : Exception()