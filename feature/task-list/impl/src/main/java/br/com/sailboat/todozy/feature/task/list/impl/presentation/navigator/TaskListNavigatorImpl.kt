package br.com.sailboat.todozy.feature.task.list.impl.presentation.navigator

import android.content.Context
import br.com.sailboat.todozy.feature.navigation.android.TaskListNavigator
import br.com.sailboat.todozy.feature.task.list.impl.presentation.startTaskListActivity

internal class TaskListNavigatorImpl : TaskListNavigator {
    override fun navigateToTaskList(context: Context) {
        context.startTaskListActivity()
    }
}
