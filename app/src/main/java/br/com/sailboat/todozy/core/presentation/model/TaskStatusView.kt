package br.com.sailboat.todozy.core.presentation.model

import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.features.tasks.domain.model.TaskStatus

enum class TaskStatusView(val description: Int) {

    NOT_DONE(R.string.task_not_done), DONE(R.string.task_done);

    companion object {
        fun getFromTaskStatus(status: TaskStatus): TaskStatusView {
            return if (status == TaskStatus.DONE) DONE else NOT_DONE
        }
    }

}