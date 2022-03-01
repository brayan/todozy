package br.com.sailboat.todozy.domain.model

import br.com.sailboat.todozy.domain.model.TaskFieldsConditions

class TaskFieldsException(val conditions: List<TaskFieldsConditions>) : Exception()