package br.com.sailboat.todozy.feature.task.form.impl.presentation.navigator

import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.feature.task.form.impl.presentation.startTaskFormActivity
import br.com.sailboat.todozy.feature.task.form.presentation.navigator.TaskFormNavigator

class TaskFormNavigatorImpl : TaskFormNavigator {

    override fun navigateToAddTaskForm(fragment: Fragment) {
        fragment.startTaskFormActivity()
    }

    override fun navigateToEditTaskForm(fragment: Fragment, taskId: Long) {
        fragment.startTaskFormActivity(taskId)
    }
}