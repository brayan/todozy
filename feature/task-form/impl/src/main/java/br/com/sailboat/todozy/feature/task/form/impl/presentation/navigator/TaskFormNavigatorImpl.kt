package br.com.sailboat.todozy.feature.task.form.impl.presentation.navigator

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import br.com.sailboat.todozy.feature.navigation.android.TaskFormNavigator
import br.com.sailboat.todozy.feature.task.form.impl.presentation.startTaskFormActivity

class TaskFormNavigatorImpl : TaskFormNavigator {

    override fun navigateToAddTask(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
    ) {
        context.startTaskFormActivity(launcher)
    }

    override fun navigateToEditTask(
        context: Context,
        taskId: Long,
        launcher: ActivityResultLauncher<Intent>,
    ) {
        context.startTaskFormActivity(taskId, launcher)
    }
}
