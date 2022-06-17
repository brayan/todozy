package br.com.sailboat.todozy.feature.navigation.android

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

interface TaskDetailsNavigator {
    fun navigateToTaskDetails(
        context: Context,
        taskId: Long,
        launcher: ActivityResultLauncher<Intent>,
    )
}
