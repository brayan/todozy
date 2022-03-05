package br.com.sailboat.todozy.feature.task.list.impl.presentation.navigator

import android.content.Context
import br.com.sailboat.todozy.feature.task.list.impl.presentation.startTaskListActivity
import br.com.sailboat.todozy.feature.task.list.presentation.navigator.TaskListNavigator

class TaskListNavigatorImpl: TaskListNavigator {

    override fun navigateToTaskList(context: Context) {
        context.startTaskListActivity()
    }

}