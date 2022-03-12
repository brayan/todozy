package br.com.sailboat.todozy.feature.task.details.impl.presentation.navigator

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import br.com.sailboat.todozy.feature.task.details.impl.presentation.startTaskDetailsActivity
import br.com.sailboat.todozy.feature.task.details.presentation.navigator.TaskDetailsNavigator

class TaskDetailsNavigatorImpl : TaskDetailsNavigator {

    override fun navigateToTaskDetails(
        context: Context,
        taskId: Long,
        launcher: ActivityResultLauncher<Intent>,
    ) {
        context.startTaskDetailsActivity(taskId, launcher)
    }

}