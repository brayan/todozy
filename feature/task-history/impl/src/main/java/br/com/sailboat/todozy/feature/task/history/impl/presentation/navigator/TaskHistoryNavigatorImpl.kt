package br.com.sailboat.todozy.feature.task.history.impl.presentation.navigator

import android.content.Context
import br.com.sailboat.todozy.feature.navigation.android.TaskHistoryNavigator
import br.com.sailboat.todozy.feature.task.history.impl.presentation.startTaskHistoryActivity

internal class TaskHistoryNavigatorImpl : TaskHistoryNavigator {
    override fun navigateToTaskHistory(context: Context) {
        context.startTaskHistoryActivity()
    }
}
