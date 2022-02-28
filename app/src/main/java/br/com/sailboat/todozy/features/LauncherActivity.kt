package br.com.sailboat.todozy.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import br.com.sailboat.todozy.features.settings.domain.usecase.CheckAndSetUpInitialSettingsUseCase
import br.com.sailboat.todozy.features.tasks.presentation.list.startTaskListActivity
import br.com.sailboat.todozy.utility.android.log.log
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LauncherActivity : AppCompatActivity() {

    val checkAndSetUpInitialSettingsUseCase: CheckAndSetUpInitialSettingsUseCase by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.coroutineScope.launch {
            try {
                checkAndSetUpInitialSettingsUseCase()
                startTaskListActivity()
                finish()
            } catch (e: Exception) {
                e.log()
            }
        }
    }

}