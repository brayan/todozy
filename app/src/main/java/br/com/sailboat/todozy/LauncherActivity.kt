package br.com.sailboat.todozy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import br.com.sailboat.todozy.feature.settings.impl.domain.usecase.CheckAndSetUpInitialSettingsUseCase
import br.com.sailboat.todozy.feature.navigation.android.TaskListNavigator
import br.com.sailboat.todozy.utility.android.log.log
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LauncherActivity : AppCompatActivity() {

    private val checkAndSetUpInitialSettingsUseCase: CheckAndSetUpInitialSettingsUseCase by inject()
    private val taskListNavigator: TaskListNavigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.coroutineScope.launch {
            try {
                checkAndSetUpInitialSettingsUseCase()
                taskListNavigator.navigateToTaskList(this@LauncherActivity)
                finish()
            } catch (e: Exception) {
                e.log()
            }
        }
    }
}
