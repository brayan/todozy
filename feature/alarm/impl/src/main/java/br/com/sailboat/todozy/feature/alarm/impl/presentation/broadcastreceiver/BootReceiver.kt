package br.com.sailboat.todozy.feature.alarm.impl.presentation.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import br.com.sailboat.todozy.feature.alarm.domain.usecase.ScheduleAlarmUpdatesUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.ScheduleAllAlarmsUseCase
import br.com.sailboat.todozy.utility.android.log.log
import br.com.sailboat.todozy.utility.android.log.logDebug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class BootReceiver : BroadcastReceiver(), KoinComponent {
    val scheduleAlarmUpdatesUseCase: ScheduleAlarmUpdatesUseCase by inject()
    val scheduleAllAlarmsUseCase: ScheduleAllAlarmsUseCase by inject()

    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            onBootCompleted()
        }
    }

    private fun onBootCompleted() {
        "${javaClass.simpleName}.onBootCompleted()".logDebug()
        GlobalScope.launch(Dispatchers.Main) {
            try {
                scheduleAlarmUpdatesUseCase()
                scheduleAllAlarmsUseCase()
            } catch (e: Exception) {
                e.log()
            }
        }
    }
}
