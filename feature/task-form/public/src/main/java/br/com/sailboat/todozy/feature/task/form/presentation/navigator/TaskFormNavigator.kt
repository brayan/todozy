package br.com.sailboat.todozy.feature.task.form.presentation.navigator

import androidx.fragment.app.Fragment

interface TaskFormNavigator {
    fun navigateToAddTaskForm(fragment: Fragment)
    fun navigateToEditTaskForm(fragment: Fragment, taskId: Long)
}