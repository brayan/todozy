package br.com.sailboat.todozy.ui.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import br.com.sailboat.todozy.data.helper.AlarmManagerHelper
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.ui.helper.log

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        intent?.run {
            if (isBootCompleted(this)) {
                initAlarms(context)
            }
        }
    }

    private fun isBootCompleted(intent: Intent): Boolean {
        return intent.action == "android.intent.action.BOOT_COMPLETED"
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