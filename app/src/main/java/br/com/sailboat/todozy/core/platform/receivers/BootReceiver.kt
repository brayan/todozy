package br.com.sailboat.todozy.core.platform.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import br.com.sailboat.todozy.core.extensions.log
import br.com.sailboat.todozy.core.extensions.logDebug
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.ScheduleAlarmUpdates
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.ScheduleAllAlarms
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BootReceiver : BroadcastReceiver(), KoinComponent {

    val scheduleAlarmUpdates: ScheduleAlarmUpdates by inject()
    val scheduleAllAlarms: ScheduleAllAlarms by inject()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            onBootCompleted()
        }
    }

    private fun onBootCompleted() {
        "${javaClass.simpleName}.onBootCompleted()".logDebug()
        GlobalScope.launch(Dispatchers.Main) {
            try {
                scheduleAlarmUpdates()
                scheduleAllAlarms()
            } catch (e: Exception) {
                e.log()
            }
        }
    }

}