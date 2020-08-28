package br.com.sailboat.todozy.core.platform.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import br.com.sailboat.todozy.core.extensions.log
import br.com.sailboat.todozy.core.platform.AlarmManagerHelper
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import org.koin.core.KoinComponent

class BootReceiver : BroadcastReceiver(), KoinComponent {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            initAlarms(context)
        }
    }

    private fun initAlarms(context: Context) {
        try {
            initTaskAlarms(context)
            initUpdateTasksAlarms(context)
        } catch (e: Exception) {
            e.log()
        }
    }

    private fun initTaskAlarms(context: Context) {
        // val tasks = TaskRepositoryImpl(context).getTasksWithAlarms()

//        for (task in tasks) {
//            initAlarm(context, task)
//        }
    }

    private fun initAlarm(context: Context, task: Task) {
        //val alarm = AlarmSQLite(context).getAlarmByTask(task.id)
        //AlarmManagerHelper(context).setNextValidAlarm(task, alarm?.mapToAlarm())
    }

    private fun initUpdateTasksAlarms(context: Context) {
        AlarmManagerHelper(context).setAlarmUpdateTasks()
    }

}