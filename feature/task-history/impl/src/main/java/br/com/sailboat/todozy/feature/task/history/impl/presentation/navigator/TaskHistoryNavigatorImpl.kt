package br.com.sailboat.todozy.feature.task.history.impl.presentation.navigator

import android.content.Context
import br.com.sailboat.todozy.feature.task.history.impl.presentation.startTaskHistoryActivity
import br.com.sailboat.todozy.feature.task.history.presentation.navigator.TaskHistoryNavigator

class TaskHistoryNavigatorImpl : TaskHistoryNavigator {

    override fun navigateToTaskHistory(context: Context) {
        context.startTaskHistoryActivity()
    }

}