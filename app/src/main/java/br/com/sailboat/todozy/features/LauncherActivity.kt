package br.com.sailboat.todozy.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import br.com.sailboat.todozy.core.extensions.log
import br.com.sailboat.todozy.features.settings.domain.usecase.CheckAndSetUpInitialSettings
import br.com.sailboat.todozy.features.tasks.presentation.list.startTaskListActivity
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LauncherActivity : AppCompatActivity() {

    val checkAndSetUpInitialSettings: CheckAndSetUpInitialSettings by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.coroutineScope.launch {
            try {
                checkAndSetUpInitialSettings()
                startTaskListActivity()
                finish()
            } catch (e: Exception) {
                e.log()
            }
        }
    }

}