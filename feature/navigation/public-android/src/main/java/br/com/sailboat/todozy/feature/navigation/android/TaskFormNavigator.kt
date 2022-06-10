package br.com.sailboat.todozy.feature.navigation.android

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