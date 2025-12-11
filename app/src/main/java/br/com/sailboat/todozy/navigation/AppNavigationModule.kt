package br.com.sailboat.todozy.navigation

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import br.com.sailboat.todozy.feature.navigation.android.HomeDestination
import br.com.sailboat.todozy.feature.navigation.android.SettingsNavigator
import br.com.sailboat.todozy.feature.navigation.android.TaskHistoryNavigator
import br.com.sailboat.todozy.feature.navigation.android.TaskListNavigator
import br.com.sailboat.todozy.home.HomeActivity
import org.koin.core.module.Module
import org.koin.dsl.module

private fun Context.startHome(destination: HomeDestination) {
    val intent = HomeActivity.createIntent(this, destination)
    startActivity(intent)
}

private fun ActivityResultLauncher<Intent>.launchHome(
    context: Context,
    destination: HomeDestination,
) {
    val intent = HomeActivity.createIntent(context, destination)
    launch(intent)
}

internal val appNavigationModule: List<Module> = listOf(
    module {
        factory<TaskListNavigator> {
            object : TaskListNavigator {
                override fun navigateToTaskList(context: Context) {
                    context.startHome(HomeDestination.TASKS)
                }
            }
        }

        factory<TaskHistoryNavigator> {
            object : TaskHistoryNavigator {
                override fun navigateToTaskHistory(context: Context) {
                    context.startHome(HomeDestination.HISTORY)
                }
            }
        }

        factory<SettingsNavigator> {
            object : SettingsNavigator {
                override fun navigateToSettings(
                    context: Context,
                    launcher: ActivityResultLauncher<Intent>,
                ) {
                    launcher.launchHome(context, HomeDestination.SETTINGS)
                }
            }
        }
    },
)
