package br.com.sailboat.todozy.feature.task.details.impl.presentation.navigator

import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.feature.task.details.impl.presentation.startTaskDetailsActivity
import br.com.sailboat.todozy.feature.task.details.presentation.navigator.TaskDetailsNavigator

class TaskDetailsNavigatorImpl: TaskDetailsNavigator {

    override fun navigateToTaskDetails(fragment: Fragment, taskId: Long) {
        fragment.startTaskDetailsActivity(taskId)
    }

}