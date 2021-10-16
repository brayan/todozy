package br.com.sailboat.todozy.core.platform.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import br.com.sailboat.todozy.core.extensions.log
import br.com.sailboat.todozy.core.extensions.logDebug
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.ScheduleAllAlarmsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ScheduleAlarmsReceiver : BroadcastReceiver(), KoinComponent {

    val scheduleAllAlarmsUseCase: ScheduleAllAlarmsUseCase by inject()

    override fun onReceive(context: Context, intent: Intent) {
        "${javaClass.simpleName}.onReceive()".logDebug()
        GlobalScope.launch(Dispatchers.Main) {
            try {
                scheduleAllAlarmsUseCase()
            } catch (e: Exception) {
                e.log()
            }
        }
    }

}