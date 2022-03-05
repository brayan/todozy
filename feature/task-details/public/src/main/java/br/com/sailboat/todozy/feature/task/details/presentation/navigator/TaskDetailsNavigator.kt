package br.com.sailboat.todozy.feature.task.details.presentation.navigator

import androidx.fragment.app.Fragment

interface TaskDetailsNavigator {
    fun navigateToTaskDetails(fragment: Fragment, taskId: Long)
}