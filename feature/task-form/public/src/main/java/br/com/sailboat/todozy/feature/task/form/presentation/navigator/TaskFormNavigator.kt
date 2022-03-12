package br.com.sailboat.todozy.feature.task.form.presentation.navigator

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

interface TaskFormNavigator {
    fun navigateToAddTask(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
    )

    fun navigateToEditTask(
        context: Context,
        taskId: Long,
        launcher: ActivityResultLauncher<Intent>,
    )
}