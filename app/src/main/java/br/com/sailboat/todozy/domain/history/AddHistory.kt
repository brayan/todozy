package br.com.sailboat.todozy.domain.history

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskStatus


class AddHistory {

    suspend operator fun invoke(task: Task, status: TaskStatus) {

    }

}